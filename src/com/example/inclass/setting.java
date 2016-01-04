/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * @author yushao ,Seongman Kim
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.devsmart.android.ui.HorizontalListView;
import com.mysql.jdbc.ResultSet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class setting {

	private String uid;
	private mysql ms;
	private String im;
	TextView name;
	TextView email;
	ImageView imageview;
	//private Button updata;

	ProgressDialog progressDialog;
	UserInfo userinfo;

	RadioGroup radgroup;
	ArrayList<ClassInfo> Classinfolist;
	Bitmap bp;

	LinearLayout content_layout;
	
	String cid_for_comment;
	ListView lv;


	ClassListAdapter classListAdapter;


	//private ArrayList<ClassInfo> Classinfolist;

	private Context context;
	InClass in;

/*
 * build new constructor. 
 */
	public  setting (String uid_,mysql ms_,Context context_,ArrayList<ClassInfo> Classinfolist_,
			LinearLayout content_layout,ListView lv,InClass in1)
	{
		
		in=in1;
		uid=uid_;
		ms=ms_;
		context=context_;
		im="";
		Classinfolist=Classinfolist_;
		cid_for_comment = "0";

		progressDialog = new ProgressDialog(context); 

		this.content_layout = content_layout;
		this.lv=lv;
		classListAdapter = new ClassListAdapter(context, Classinfolist_, ms, uid);
		lv.setAdapter(classListAdapter);

	}
	/*
	 * load the all information from database. 
	 */
	public void load(TextView name_,TextView email_,RadioGroup radgroup_, ImageView _imageview)
	{
		name=name_;
		email=email_;
		radgroup=radgroup_;
		imageview=_imageview;
		new MyTaskload().execute("");


	}
	/*
	 * return user information
	 */
	public UserInfo getuserinfo()
	{
		return userinfo;
	}
	/*
	 * updata the class list
	 */
	public void updata_classlist(ArrayList<ClassInfo> Classinfolist_)
	{
		Classinfolist=Classinfolist_;
		classListAdapter = new ClassListAdapter(context, Classinfolist_, ms, uid);
		lv.setAdapter(classListAdapter);
		
		
	}


	private class MyTaskload extends AsyncTask<String, Integer, String>
	{

		@Override  
		protected void onPreExecute() {  
			//Log.i(TAG, "onPreExecute() called");  
			//			progressDialog.setTitle("Load");
			//			progressDialog.show();
		}  

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			publishProgress(0);
			try {
				ResultSet rs=null;
				String sql="SELECT * FROM senior_project.User where uid='"+uid+"'";
				rs=ms.execQuery(sql);
				while(rs.next())
				{
					String username=rs.getString("username");
					String realname=rs.getString("realname");
					String email=rs.getString("umail");
					String g=rs.getString("gender");
					String photoname=rs.getString("photo");



					userinfo= new UserInfo(username,realname,email,"",Classinfolist,g,photoname);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



			InputStream inputStream = null;
			//Bitmap imgBitmap = null;
			try {
				//URL url = new URL(params[0]);
				URL url=new URL("http://senior-07.eng.utah.edu/Ument/"+userinfo.getPhoto().toString());
				if(url != null) {
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(2000);
					connection.setDoInput(true);
					connection.setRequestMethod("GET");
					int code = connection.getResponseCode();
					if(200 == code) {
						inputStream = connection.getInputStream();
						bp = BitmapFactory.decodeStream(inputStream);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			publishProgress(100); 

			return null;
		}


		@Override
		protected void onPostExecute(String result) {
			imageview.setImageBitmap(bp);



			name.setText(userinfo.getRealName());
			email.setText(userinfo.getumail());
		}
	}
	/*
	 * update  the user's photo. 
	 */
	private class MyTaskupdata extends AsyncTask<String, Integer, String> {

		@Override  
		protected void onPreExecute() {  
			//Log.i(TAG, "onPreExecute() called");  
			progressDialog.setTitle("Updata");
			progressDialog.show();
		}  


		@Override
		protected String doInBackground(String... params) {


			String pname=uid;


			final File ff= new File(im);

			String exten=getExtensionName(ff.getName());
			Bitmap spic=getSmallBitmap(im);

			final File file = new File(Environment.getExternalStorageDirectory()+"/"+pname);
			FileOutputStream fOut;
			try {
				fOut = new FileOutputStream(file);
				spic.compress(Bitmap.CompressFormat.PNG, 85, fOut);
				fOut.flush();
				fOut.close();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				Log.e(null, "Save file error!");
			}


			pname=pname+"."+exten;

			final HttpAssist ha= new HttpAssist();
			ha.uploadument();
			ha.setid(pname);
			ha.uploadFile(file);
			if(file.exists()){
				file.delete();
				Log.i("OK","delete");
			}


			String sql="UPDATE `senior_project`.`User` SET `photo`='" +pname+"' WHERE `uid`='"+uid+"'";


			Log.e("sql",sql);

			try 
			{
				int rows=ms.execUpdate(sql);

				Log.e("rows",rows+"");
			}
			catch (SQLException e) 
			{

			}


			return null;
		}
		@Override
		protected void onPostExecute(String result) {  
			FileCache ff= new FileCache(context);
			ff.clear();

			progressDialog.dismiss();


		}

	}



	public void setpic(String s)
	{
		im=s;
		new MyTaskupdata().execute("");
	}


	public static String getExtensionName(String filename1) { 
		//String ext="";
		String filename=filename1;
		if ((filename != null) && (filename.length() > 0)) {   
			int dot = filename.lastIndexOf('.');   
			if ((dot >-1) && (dot < (filename.length() - 1))) {   
				return filename.substring(dot + 1);   
			}   
		}
		filename = filename1.replaceAll(filename ,"");
		return filename; 
	}

	public static Bitmap getSmallBitmap(String filePath) {  

		final BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inJustDecodeBounds = true;  
		BitmapFactory.decodeFile(filePath, options);  

		// Calculate inSampleSize  
		options.inSampleSize = calculateInSampleSize(options, 200,200);  

		// Decode bitmap with inSampleSize set  
		options.inJustDecodeBounds = false;  

		Bitmap bm = BitmapFactory.decodeFile(filePath, options);  
		if(bm == null){  
			return  null;  
		}  

		return bm ;  

	} 
	private static int calculateInSampleSize(BitmapFactory.Options options,  
			int reqWidth, int reqHeight) {  
		// Raw height and width of image  
		final int height = options.outHeight;  
		final int width = options.outWidth;  
		int inSampleSize = 1;  

		if (height > reqHeight || width > reqWidth) {  

			// Calculate ratios of height and width to requested height and  
			// width  
			final int heightRatio = Math.round((float) height  
					/ (float) reqHeight);  
			final int widthRatio = Math.round((float) width / (float) reqWidth);  

			// Choose the smallest ratio as inSampleSize value, this will  
			// guarantee  
			// a final image with both dimensions larger than or equal to the  
			// requested height and width.  
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;  
		}
		return inSampleSize;  
	}  




	/**
	 * Simple adapter for class list in setting.
	 * takes setting_class_item for each item of list view.
	 * 
	 * @author k3693692000
	 *
	 */
	public class ClassListAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater;
		private ArrayList<ClassInfo> classInformations;



		private Context context;

		public ClassListAdapter(Context context, ArrayList<ClassInfo> classes, mysql ms, String myuid) {  
			this.mInflater = LayoutInflater.from(context);
			this.context = context;


			classInformations = classes;





		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return classInformations.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}



		//View studentImageListForXML;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ClassInfo classinfo_ =classInformations.get(position); 
			final String classId = classinfo_.getCid();
			cid_for_comment = classId;
			final String classInformation_ = classinfo_.getClassNumber_()+": "+classinfo_.getName()+"-"+classinfo_.getSection()+"\n"
					+classinfo_.getProfessor()+"\n"
					+classinfo_.getDay() + " " + classinfo_.getStartTime() + "-" + classinfo_.getEndTime();
			final Context newContext = context;



			convertView = new View(newContext);
			convertView = mInflater.inflate(R.layout.setting_class_item, null);
			
			final TextView content_text = (TextView) convertView.findViewById(R.id.setting_class_item_content);
			content_text.setText(classInformation_);
			content_text.setTextColor(Color.rgb(00, 00, 00));

			convertView.setOnClickListener(new View.OnClickListener() 
			{

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,ClassInformation.class);
			
					
					intent.putExtra("cid",classinfo_.getCid()+"" );
					intent.putExtra("uid", uid);
					in.startActivityForResult(intent, 5);
					
				}
				
			});

			
			return convertView;
			
		}
		
	}

}
