/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * Reply activity. 
 * @author yushao 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.inclass.Ument.MyAdapter;
import com.example.inclass.Ument.MyThread1;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/*class extends the activity. 
 * @author yushao
 *
 */
public class Reply  extends Activity {

	mysql ms;
	String myid;
	String umentid;
	String name_str;
	String time_str;
	String photo_str;
	String text_str;
	String pic_str;
	ArrayList<reply_class>data;

	
	

	PullToRefreshListView listument;

	MyAdapter adapter;
	Context context;
	Button back;
	Button commentbutton;
	Button reply_Like;
	String casenumber;
	String eid;
	String umentuid;
	String comment;
	String mode;
	Thread temp;
	private static final int MSG_SUCCESS = 0;
	private static final int MSG_FAILURE = 1;

	/*
	 * create the new activity. 
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reply_list);
		ms= new mysql();
		data=new ArrayList<reply_class>();
		Intent intent=getIntent(); 
		
		TextView comment_name= (TextView)findViewById(R.id.comment_title);
		mode=intent.getStringExtra("mode");
		if(intent.getStringExtra("casenumber").equals("1"))
		{
			comment_name.setText("Detail");	
		casenumber="1";
		umentid=intent.getStringExtra("umentid");
		name_str=intent.getStringExtra("name");
		time_str=intent.getStringExtra("time");
		text_str=intent.getStringExtra("text");
		photo_str=intent.getStringExtra("photo");
		pic_str=intent.getStringExtra("pic");
		myid=intent.getStringExtra("myid");
		eid=intent.getStringExtra("eid");
		
		umentuid=intent.getStringExtra("umentuid");
		}
		else
		{
			casenumber="0";
			umentid=intent.getStringExtra("umentid");
			myid=intent.getStringExtra("uid");
		}
		comment=intent.getStringExtra("comment");
		
	
		context= this;
		listument=(PullToRefreshListView)findViewById(R.id.reply_list);
		adapter=new MyAdapter(context);
		listument.setAdapter(adapter);
		back=(Button)findViewById(R.id.back_reply);
		commentbutton=(Button)findViewById(R.id.reply_comment);
		
		//commentbutton.setText(commentbutton.getText()+"  "+comment);

		
		new MyTask().execute("");
		
		/*
		 * set the back button Listener. 
		 */
		back.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent ;
				if(mode.equals("0"))
				{
				intent = new Intent(Reply.this,InClass.class);
				}
				else
				{
					intent = new Intent(Reply.this,myument.class);
				}
                setResult(RESULT_OK, intent);  
                Bundle conData = new Bundle();
                conData.putString("umentid", umentid);
                intent.putExtras(conData);
                setResult(RESULT_OK, intent);
				finish();
			}
			
		});
		/*
		 * set the comment button Listener
		 */
		commentbutton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, Addcommentforument.class);
				intent.putExtra("umentid",umentid);
				intent.putExtra("uid",myid);
				intent.putExtra("case","1");
				startActivityForResult(intent, 0);
				
			}
			
		});
		
		
		/*
		 * set listview the setRefreshListener
		 */
		listument.setOnRefreshListener(new OnRefreshListener<ListView>() 
				{  
			@Override  
			public void onRefresh(PullToRefreshBase<ListView> refreshView) 
			{  
				String label = DateUtils.formatDateTime(context, System.currentTimeMillis(),  
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  

				// Update the LastUpdatedLabel  
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  

					new MyTask().execute();
			}
				});  
		/*
		 * set the list view the ItemClickListener
		 */
		listument.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, Addcommentforument.class);
				if(casenumber=="1")
				{
					position=position-2;
					
				}
				else
				{
					position=position-1;
				}
				intent.putExtra("umentid",umentid);
				intent.putExtra("uid",myid);
				intent.putExtra("reply_uid",data.get(position).uid);
				intent.putExtra("reply_name",data.get(position).name);
				intent.putExtra("case","1");
				startActivityForResult(intent, 0);
			
				
			}
			
		});




	}
	
	
	private Handler mHandler = new Handler() {  
		public void handleMessage (Message msg) {//������������ui������������  
			switch(msg.what) {  
			case MSG_SUCCESS:  

	
				listument.onRefreshComplete(); 
				adapter.notifyDataSetChanged();
	        	int comment_int=data.size();
	        	commentbutton.setText("Comment"+"  "+comment_int);
	        	comment=(comment_int)+"";

				break;  

			case MSG_FAILURE:  

				break;  
			}  
		}  
	};  
	
	public void onBackPressed(){
		Intent intent = new Intent(Reply.this,InClass.class);
        setResult(RESULT_OK, intent);  
        Bundle conData = new Bundle();
        conData.putString("umentid", umentid);
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
		finish();
	}
	
	/*
	 * 
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
	       switch (requestCode)  
	        {  
	        case 0:  
	        {
	        	
	        	
	        	temp=new Thread(new MyThread());
				temp.start();

				try {
					temp.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


	        }
	        }


    }  
	
	
	
	
	
	public class MyThread implements Runnable
	{
		@Override
		public void run()
		{
			ms.connectDB();
			
			data=new ArrayList<reply_class>();
		
			ResultSet rs=null;
			String sql="select r.*, u.username,u.photo from senior_project.Ument_reply as r left join senior_project.User as u on r.uid = u.uid where r.mid="+umentid+" order by time desc; ";

			try {
				rs=ms.execQuery(sql);

				while (rs.next())
				{
					reply_class r=new reply_class();
					String content=rs.getString("content");
					String uid=rs.getString("uid");
					String time =rs.getString("time");
					String mrid=rs.getString("mrid");
					String reply_uid=rs.getString("reply_uid");
					String username=rs.getString("username");
					String userphoto=rs.getString("photo");
					
					time=time.substring(0,time.toCharArray().length-2);
					
					r.setcontent(content);
					r.setuid(uid);
					r.settime(time);
					r.setreply_uid(reply_uid);
					r.setname(username);
					r.setmrid(mrid);
					r.setphoto(userphoto);
					
					data.add(r);
				}
				sql="UPDATE `senior_project`.`Ument` SET `comment`='"+data.size()+"' WHERE `mid`='"+umentid+"'";
				ms.execUpdate(sql);
			}
			
			
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ms.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
			

		}
		
		

	}

	

/*
 * get the all reply from the database. 
 */
	private class MyTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			ms.connectDB();
			
			data=new ArrayList<reply_class>();
		
			ResultSet rs=null;
			String sql="select r.*, u.username,u.photo from senior_project.Ument_reply as r left join senior_project.User as u on r.uid = u.uid where r.mid="+umentid+" order by time desc; ";

			try {
				rs=ms.execQuery(sql);

				while (rs.next())
				{
					reply_class r=new reply_class();
					String content=rs.getString("content");
					String uid=rs.getString("uid");
					String time =rs.getString("time");
					String mrid=rs.getString("mrid");
					String reply_uid=rs.getString("reply_uid");
					String username=rs.getString("username");
					String userphoto=rs.getString("photo");
					
					time=time.substring(0,time.toCharArray().length-2);
					
					r.setcontent(content);
					r.setuid(uid);
					r.settime(time);
					r.setreply_uid(reply_uid);
					r.setname(username);
					r.setmrid(mrid);
					r.setphoto(userphoto);
					
					data.add(r);
				}
				sql="UPDATE `senior_project`.`Ument` SET `comment`='"+data.size()+"' WHERE `mid`='"+umentid+"'";
				ms.execUpdate(sql);
			}
			
			
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ms.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";


		}
		
		
		@Override  
		protected void onPostExecute(String result) {  
			listument.onRefreshComplete(); 
			adapter.notifyDataSetChanged();
        	int comment_int=data.size();
        	commentbutton.setText("Comment"+"  "+comment_int);
        	comment=(comment_int)+"";
			try {
				ms.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}


	public class MyAdapter extends BaseAdapter {  
		private LayoutInflater mInflater;// 动态布局映射 
		Context context;

		public MyAdapter(Context context) { 
			this.context=context;
			this.mInflater = LayoutInflater.from(context);  
		}  

		@Override  
		public int getCount() {  
			
			if(casenumber=="1")
			{
				return data.size()+1;
			}
			else 
			{
				return data.size();
			}
		}  

		@Override  
		public Object getItem(int arg0) {  
			return null;  
		}  

		@Override  
		public long getItemId(int arg0) {  
			return 0;  
		}  
		@Override 
		public boolean isEnabled(int position) {
			if(casenumber=="1")
			{
			if(position==0)
			{
			return false;
			}
			}
			return true;
		
		}


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ImageLoader imageLoader=new ImageLoader(context);
			Button addevent;
			ImageView replypic;
			
			if(casenumber=="1")
			{
			if(position==0)
			{
				
				ImageView userpic1 = null;
				if(photo_str.equals("0"))
				{
					convertView = mInflater.inflate(R.layout.replyitem1, null);


					TextView username= (TextView)convertView.findViewById(R.id.reusername_ument);
					TextView text=(TextView)convertView.findViewById(R.id.retext_ument);
					TextView time=(TextView)convertView.findViewById(R.id.retime_ument);
					addevent=(Button)convertView.findViewById(R.id.add_event3);
					if(!eid.equals("null"))
					{
						addevent.setVisibility(View.VISIBLE);  
					}

					userpic1=(ImageView)convertView.findViewById(R.id.relist_image);

					username.setText(name_str);
					time.setText(time_str);
					text.setText(text_str);
					imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+pic_str, userpic1);
					addevent.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, Additem_event.class);
							intent.putExtra("eid", eid);
							intent.putExtra("uid", myid);
							intent.putExtra("type", "add");
							
							startActivity(intent);  
							
						}
						
					});

					userpic1.setOnClickListener (new View.OnClickListener() {
						
						public void onClick(View v) {
							
							Intent intent = new Intent(context, UserInformationActivity.class);

							intent.putExtra("targetUid", umentuid);
							intent.putExtra("userId", myid);
							startActivity(intent);
						
						}
					});
				}
				else
				{
					convertView = mInflater.inflate(R.layout.replyitem2, null);
					//convertView.setEnabled(false);

					TextView username= (TextView)convertView.findViewById(R.id.reusername_ument2);
					TextView text=(TextView)convertView.findViewById(R.id.retext_ument2);
					TextView time=(TextView)convertView.findViewById(R.id.retime_ument2);
					ImageView pic=(ImageView)convertView.findViewById(R.id.reimageview_event2);
					addevent=(Button)convertView.findViewById(R.id.add_event4);
					ImageView userpic2=(ImageView)convertView.findViewById(R.id.relist_image2);
					if(!eid.equals("null"))
					{
						addevent.setVisibility(View.VISIBLE);  
					}
					
					
					username.setText(name_str);
					time.setText(time_str);
					text.setText(text_str);
					imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+pic_str, userpic2);
					imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+photo_str, pic);
					
					addevent.setOnClickListener(new View.OnClickListener()
					{

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, Additem_event.class);
							intent.putExtra("eid", eid);
							intent.putExtra("uid", myid);
							intent.putExtra("type", "add");
							
							startActivity(intent);  
							
						}
						
					});

					userpic2.setOnClickListener (new View.OnClickListener() {
						
						public void onClick(View v) {
							
							Intent intent = new Intent(context, UserInformationActivity.class);

							intent.putExtra("targetUid", umentuid);
							intent.putExtra("userId", myid);
							startActivity(intent);
						
						}
					});
				}


				
		

			}
			else
			{
				convertView = mInflater.inflate(R.layout.replyitem, null);
				replypic=(ImageView)convertView.findViewById(R.id.replyitempic);
				TextView replycontent=(TextView)convertView.findViewById(R.id.replytext);
				String s="";
				if(data.get(position-1).reply_uid!=null)
				{
					for (reply_class r:data)
					{
						if(r.uid.equals(data.get(position-1).reply_uid))
						{
							data.get(position-1).setreply_name(r.name);
						}
					}
					s=data.get(position-1).name+"\n"+data.get(position-1).time+"\n"+"@"+data.get(position-1).reply_name+": "+data.get(position-1).content;
				}
				else
				{
				 s=data.get(position-1).name+"\n"+data.get(position-1).time+"\n"+data.get(position-1).content;
				}
				
				imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+data.get(position-1).photo,replypic );
				replycontent.setText(s);
				replypic.setOnClickListener (new View.OnClickListener() {
					
					public void onClick(View v) {
						
						Intent intent = new Intent(context, UserInformationActivity.class);

						intent.putExtra("targetUid", data.get(position).uid);
						intent.putExtra("userId", myid);
						startActivity(intent);
					
					}
				});

			}
			}
			else 
			{
				
				convertView = mInflater.inflate(R.layout.replyitem, null);
				replypic=(ImageView)convertView.findViewById(R.id.replyitempic);
				TextView replycontent=(TextView)convertView.findViewById(R.id.replytext);
				String s="";
				if(data.get(position).reply_uid!=null)
				{
					for (reply_class r:data)
					{
						if(r.uid.equals(data.get(position).reply_uid))
						{
							data.get(position).setreply_name(r.name);
						}
					}
					s=data.get(position).name+"\n"+data.get(position).time+"\n"+"@"+data.get(position).reply_name+": "+data.get(position).content;
				}
				else
				{
				 s=data.get(position).name+"\n"+data.get(position).time+"\n"+data.get(position).content;
				}
				
				imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+data.get(position).photo,replypic );
				replycontent.setText(s);
				replypic.setOnClickListener (new View.OnClickListener() {
					
					public void onClick(View v) {
						
						Intent intent = new Intent(context, UserInformationActivity.class);

						intent.putExtra("targetUid", data.get(position).uid);
						intent.putExtra("userId", myid);
						startActivity(intent);
					
					}
				});

				
			}
			
			


			return convertView;
		}  
	}  

}
