package com.example.inclass;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;





import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Activity for user information page for others.
 * leads to the page of other users' information page.
 * @author k3693692000
 */
public class UserInformationActivity extends Activity{
	mysql ms;
	String username;
	String realname;
	String email;
	String myuid;
	String targetUid;
	String gender;
	String photoname;
	// shows username
	TextView title_Text;
	TextView username_Text;
	TextView email_Text;
	ListView classlistview;
	Context context;

	Button send_message;

	UserInfo userinfo;
	ArrayList<ClassInfo> Classinfolist;
	Bitmap bp;
	ImageView imageview;
	ChatListWriter chatListWriter;
	RelativeLayout myument;
	Button back;


	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;

		Intent intent=getIntent(); 
		myuid=intent.getStringExtra("userId").toString();
		targetUid=intent.getStringExtra("targetUid").toString();
		
		setContentView(R.layout.user_information_page);
		myument=(RelativeLayout)findViewById(R.id.myument);
		title_Text = (TextView) findViewById(R.id.userinfo_title_text);
		imageview = (ImageView) findViewById(R.id.userinfo_setting_image);
		email_Text = (TextView) findViewById(R.id.userinfo_settingEmail);
		username_Text = (TextView) findViewById(R.id.userinfo_settingname);
		classlistview = (ListView) findViewById(R.id.userinfo_classlist);
		send_message = (Button) findViewById(R.id.userinfo_send_message_button);
		Classinfolist = new ArrayList<ClassInfo>();
		back =(Button) findViewById(R.id.user_infor_back);
		// send message to user.
		// it requires target id and user id.

		ms = new mysql();
		new LoadAsync().execute("");
	}

	private class LoadAsync extends AsyncTask<String, Integer, String>
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
			ms.connectDB();
			try {
				ResultSet rs=null;
				String sql="SELECT * FROM senior_project.User where uid='"+targetUid+"'";
				rs=ms.execQuery(sql);

				while(rs.next())
				{
					username=rs.getString("username");
					realname=rs.getString("realname");
					email=rs.getString("umail");
					gender=rs.getString("gender");
					photoname=rs.getString("photo");

					userinfo= new UserInfo(username,realname,email,"",Classinfolist,gender,photoname);
				}
				rs = null; //refreshing rs;
				sql="select * from senior_project.Course where cid in (select cid from senior_project.Taking where uid = '"+targetUid+"')";
				rs=ms.execQuery(sql);
				while(rs.next())
				{
					ClassInfo c= new ClassInfo();

					String cname;
					cname=(String) rs.getObject("cname");
					String cnumber;
					cnumber=(String) (rs.getObject("number")+"");
					String day=(String)rs.getObject("date");
					String startTime=(String)rs.getObject("stime");
					String endTime=(String)rs.getObject("etime");
					String cid=(String)(rs.getObject("cid")+"");
					String professor=(String)(rs.getObject("professor")+"");
					String location =(String)(rs.getObject("location")+"");
					c.setName(cname);
					c.setCid(cid);
					c.setDay(day);
					c.setClassNumber(cnumber);
					c.setTime(startTime, endTime);
					c.setSection((String)(rs.getObject("section")+""));
					c.setDepartment((String) rs.getObject("department"));
					c.setProfessor(professor);
					c.setLocation(location);
					Classinfolist.add(c);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
			myument.setOnClickListener(new View.OnClickListener() 
			{ @Override 
				
				public void onClick(View v) 
			{ Intent intent = new Intent(context, myument.class);
			intent.putExtra("userId", myuid);
			intent.putExtra("targetUid", targetUid);
			startActivity(intent);
			}
			}
			);
			back.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v) {
					try {
						ms.closeConn();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finish();
					
				}
				
			});
			
			send_message.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					
					
					chatListWriter = new ChatListWriter(myuid, targetUid);
					Intent intent = new Intent(context, ChatClient.class);
					intent.putExtra("un", myuid);
					intent.putExtra("toWhom", targetUid);
					intent.putExtra("name", realname);
					startActivity(intent);

//					// TODO Auto-generated method stub
//					Intent newIntent = new Intent(context, ChatClient.class);
//					newIntent.putExtra("un", myuid);
//					dsaklndkasn
//					newIntent.putExtra("toWhom", targetUid);
//					newIntent.putExtra("name", realname);
//					context.startActivity(newIntent);

				}
			});
			
			




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
			try {
				ms.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}


		@Override
		protected void onPostExecute(String result) {
			imageview.setImageBitmap(bp);

			title_Text.setText(username);
			username_Text.setText(realname);
			email_Text.setText(email);

			ClassListAdapter classListAdapter = new ClassListAdapter(context, Classinfolist);
			classlistview.setAdapter(classListAdapter);
		}
	}


	public class ClassListAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater;
		private ArrayList<ClassInfo> classInformations;
		private Context context;

		public ClassListAdapter(Context context, ArrayList<ClassInfo> classes) {  
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
			final String classInformation_ = classinfo_.getClassNumber_()+": "+classinfo_.getName()+"-"+classinfo_.getSection()+"\n"
					+classinfo_.getProfessor()+"\n"
					+classinfo_.getDay() + " " + classinfo_.getStartTime() + "-" + classinfo_.getEndTime();
			final Context newContext = context;
			//			ClassInformationPage classInformationPage;

			convertView = new View(newContext);
			convertView = mInflater.inflate(R.layout.setting_class_item, null);

			final TextView content_text = (TextView) convertView.findViewById(R.id.setting_class_item_content);
			content_text.setText(classInformation_);
			
			convertView.setOnClickListener(new View.OnClickListener() 
			{

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,ClassInformation.class);
			
					
					intent.putExtra("cid",classinfo_.getCid()+"" );
					intent.putExtra("uid", myuid);
					startActivity(intent);
					
				}
				
			});

			return convertView;  
		}

	}
}
