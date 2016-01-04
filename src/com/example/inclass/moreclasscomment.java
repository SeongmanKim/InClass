/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 *  * @author yushao, k3693692000
 * get all the comment (new Activity)
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class moreclasscomment extends Activity {
	String cid;
	mysql ms;
	ProgressDialog progressDialog;
	ListView lvcomment;
	MyAdapter myadpter;
	//ImageView photo;
	Context context;
	String uid;
	Button back;

	ArrayList<HashMap<String, Object>>mData;
	/*
	 * create the new activity. 
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ms=new mysql();
		setContentView(R.layout.commentview);
		context=this;
		progressDialog = new ProgressDialog(this); 

		Intent intent=getIntent(); 



		myadpter= new MyAdapter(this);
		cid=intent.getStringExtra("cid").toString();
		uid=intent.getStringExtra("uid").toString();
		mData= new ArrayList<HashMap<String, Object>>() ;
		lvcomment=(ListView)findViewById(R.id.commentview);

		back =(Button)findViewById(R.id.commentback);



		new MyTask().execute("");

		back.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				finish();

			}

		});
	}

	/*
	 * new adpeter for list view. 
	 */

	public class MyAdapter extends BaseAdapter
	{

		private LayoutInflater mInflater;
		public MyAdapter(Context context) {  
			this.mInflater = LayoutInflater.from(context);  
		}  

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
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

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			convertView = mInflater.inflate(R.layout.commentitem, null);
			//ImageLoader imageLoader=new ImageLoader(context);

			TextView username= (TextView)convertView.findViewById(R.id.username_comment);
			TextView username_for_frame= (TextView)convertView.findViewById(R.id.username_comment_for_frame);
			TextView text=(TextView)convertView.findViewById(R.id.text_comment);
			TextView time=(TextView)convertView.findViewById(R.id.time_comment);
			//photo=(ImageView)convertView.findViewById(R.id.list_image3);

			username.setText(mData.get(position).get("username").toString());
			username_for_frame.setText(mData.get(position).get("username").toString());
			time.setText(mData.get(position).get("time").toString());
			text.setText(mData.get(position).get("content").toString());
			//imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("photo").toString(), photo);

			username.setOnClickListener (new View.OnClickListener() {

				public void onClick(View v) {

					Intent intent = new Intent(context, UserInformationActivity.class);

					intent.putExtra("targetUid", mData.get(position).get("uid").toString());
					intent.putExtra("userId", uid);
					startActivity(intent);

				}
			});

			//				photo.setOnClickListener (new View.OnClickListener() {
			//					
			//					public void onClick(View v) {
			//						
			//						Intent intent = new Intent(context, UserInformationActivity.class);
			//
			//						intent.putExtra("targetUid", mData.get(position).get("uid").toString());
			//						intent.putExtra("userId", uid);
			//						startActivity(intent);
			//					
			//					}
			//				});









			return convertView;
		}

	}
	/*
	 * get all comment from the data base. 
	 */

	private class MyTask extends AsyncTask<String, Integer, String> {

		@Override  
		protected void onPreExecute() {  
			//Log.i(TAG, "onPreExecute() called");  
			progressDialog.setTitle("Load");
			progressDialog.show();
		}  

		@Override
		protected String doInBackground(String... params) {

			//progressDialog.setTitle("Load");

			ms.connectDB();


			publishProgress(0);
			String sql="select u.photo,u.uid, u.username, c.* from senior_project.Course_comment as c left join senior_project.User as u on c.uid = u.uid where cid='"+cid+"'order by time desc;";
			//ResultSet rs=null;
			try {
				ResultSet rs=null;
				rs=ms.execQuery(sql);
				while(rs.next())
				{

					HashMap<String, Object> map = null;  
					map = new HashMap<String, Object>();
					String username=rs.getString("username");
					map.put("username", username);
					String content=rs.getString("content");
					map.put("content", content);
					String time =rs.getString("time");
					time=time.substring(0,time.toCharArray().length-2);
					map.put("time", time);
					map.put("uid", rs.getString("uid"));
					map.put("photo", rs.getString("photo"));

					mData.add(map);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			publishProgress(100);
			// TODO Auto-generated method stub
			return null;
		}


		@Override  
		protected void onProgressUpdate(Integer... progresses) { 
			progressDialog.setMessage(progresses[0] + "%");
			//progressDialog.setProgress(progresses[0] ); 

			//.setText("loading..." + progresses[0] + "%");

		}  

		//onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
		@Override  
		protected void onPostExecute(String result) {  

			lvcomment.setAdapter(myadpter);
			progressDialog.dismiss();

		}  
	}





}
