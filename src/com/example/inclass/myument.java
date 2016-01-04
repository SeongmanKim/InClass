package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.inclass.Ument.MyAdapter;
import com.example.inclass.Ument.MyThread;
import com.example.inclass.Ument.MyThread1;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class myument extends Activity{
	
	PullToRefreshListView listview;
	Button back;
	ArrayList<HashMap<String, Object>> mData;
	mysql ms;
	String uid;
	String targetUid;
	Context context;
	Thread temp;
    private static final int MSG_1= 0;
    private static final int MSG_2 = 1;
    MyAdapter adapter;
    String MODE;
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myument);
		context = this;
		Intent intent=getIntent(); 
		uid=intent.getStringExtra("userId").toString();
		targetUid=intent.getStringExtra("targetUid").toString();
		setupViews();
		mData=new ArrayList<HashMap<String, Object>>();
		temp=new Thread(new MyThread());
		temp.start();

		try {
			temp.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		listview.setOnRefreshListener(new OnRefreshListener<ListView>() 
				{  
			@Override  
			public void onRefresh(PullToRefreshBase<ListView> refreshView) 
			{  
				String label = DateUtils.formatDateTime(context, System.currentTimeMillis(),  
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("label");  


				if (PullToRefreshBase.Mode.PULL_FROM_START == listview.getCurrentMode()) 
				{
					MODE="pull";
					temp=new Thread(new MyThread1());
					temp.start();

					try {
						temp.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
				} else if (PullToRefreshBase.Mode.PULL_FROM_END == listview.getCurrentMode())
				{
					MODE="end";
					temp=new Thread(new MyThread1());
					temp.start();

					try {
						temp.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
				}
				
				
			}
				});  

		listview.setMode(Mode.BOTH);
		// set the item clickLiSTENER.
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{

				Intent intent = new Intent(context, Reply.class);
				intent.putExtra("umentid",mData.get(position-1).get("umentid").toString());
				intent.putExtra("name",mData.get(position-1).get("username").toString());
				intent.putExtra("time",mData.get(position-1).get("time").toString());
				intent.putExtra("photo",mData.get(position-1).get("imgbb").toString());
				intent.putExtra("text",mData.get(position-1).get("content").toString());
				intent.putExtra("pic",mData.get(position-1).get("photo").toString());
				intent.putExtra("myid",uid);
				intent.putExtra("mode","1");
				intent.putExtra("umentuid",mData.get(position-1).get("uid").toString());
				if(mData.get(position-1).get("eid")!=null)
				{
					intent.putExtra("eid",mData.get(position-1).get("eid").toString());
				}
				else

				{
					intent.putExtra("eid","null");
				}

				intent.putExtra("casenumber","1");
				startActivity(intent);
			}
		});



		
		
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0)
		{
			Bundle res = data.getExtras();
            String result = res.getString("umentid");
            updatacomment(result);
		}
			
		}
	
	   private Handler mHandler = new Handler() {  
	        public void handleMessage (Message msg) { 
	            switch(msg.what) {  
	            case MSG_1:  
	            	
	            	listview.setAdapter(adapter);
	               
	                break;  
	  
	            case MSG_2:  
	            	listview.requestLayout();
	            	listview.onRefreshComplete(); 
					adapter.notifyDataSetChanged();
	                break;  
	            }  
	        }  
	    };  
	
	public void setupViews()
	{
		listview=(PullToRefreshListView)findViewById(R.id.myumentlistview);
		back=(Button)findViewById(R.id.myumentback);
		mData= new ArrayList<HashMap<String, Object>>() ;
		ms= new mysql();
		MODE="";
		
		adapter = new MyAdapter(context);
	}
	public void updatacomment(final String umentid)
	{
		
		
		Thread temp2 =new Thread() 
		{ 
			@Override
			public void run()
			{
				try 
				{
					ResultSet rs1= null;
					String sql1="SELECT comment FROM senior_project.Ument where mid="+umentid+"";
					rs1=ms.execQuery(sql1);
					while(rs1.next())
					{
						
						for(int i=0;i<mData.size();i++)
						{
							if(mData.get(i).get("umentid").toString().equals(umentid))
							{
								mData.get(i).put("comment", rs1.getString("comment"));
							}
						}
					}

					
				}
				catch (SQLException e) 
				{
					e.printStackTrace();
					Log.i("SQL ERROR" , "SQL ERROR : " + e.toString());
					//if(e.)


				}
			}
		};
		temp2.start();
		try
		{
			temp2.join();
		}
		catch (InterruptedException e)  
		{  
			e.printStackTrace();  
		}  
		
		
		adapter.notifyDataSetChanged();

		
	
		
	}
	
	public class MyThread implements Runnable
	{
		@Override
		public void run()
		{
			try {
				ms.connectDB();
				ResultSet rs=null;
				String sql="select u.realname, u.uid,u.photo,c.* from senior_project.Ument as c left join senior_project.User as u on c.uid = u.uid where u.uid='"+targetUid+"'order by time desc limit 0, 5;";
				rs=ms.execQuery(sql);
				//Message msg = Message.obtain();
				while(rs.next())
				{

					HashMap<String, Object> map = null;  
					map = new HashMap<String, Object>(); 
					String sql1="SELECT comment,likes FROM senior_project.Ument where mid='"+rs.getString("mid")+"'";
					ResultSet rs1=null;
					rs1=ms.execQuery(sql1);
					while(rs1.next())
					{
						map.put("comment", rs1.getString("comment"));
						map.put("like", rs1.getString("likes"));
					}

					String sql2="SELECT * FROM senior_project.Ument_like where mid="+rs.getString("mid")+" and uid="+uid+"";

					ResultSet rs2=null;
					rs2=ms.execQuery(sql2);

					if(rs2.next())
					{
						map.put("like_", "1");
					}
					else
					{
						map.put("like_", "0");
					}
					map.put("username", rs.getString("realname"));
					String time =rs.getString("time");
					time=time.substring(0,time.toCharArray().length-2);
					map.put("time", time);
					String content=rs.getString("content");
					map.put("content", content);
					map.put("uid", rs.getString("uid"));
					//String image =rs.getString("picture");
					map.put("imgbb", rs.getString("picture"));
					map.put("photo",rs.getString("photo"));
					map.put("umentid", rs.getString("mid"));
					map.put("eid",rs.getString("eid"));


					mData.add(map);
				}
				mHandler.obtainMessage(MSG_1).sendToTarget();
			}
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	
	public class MyThread1 implements Runnable
	{

		@Override
		public void run() {
			
			
			try 
			{
				String sql="";
				if(MODE=="end")
				{
					sql="select u.realname,u.uid, u.photo,c.* from senior_project.Ument as c left join senior_project.User as u on c.uid = u.uid  where time<'"+mData.get(mData.size()-1).get("time") +  " '  and u.uid='"+targetUid+"'order by time desc limit 0, 5";
				}

				else

				{
					//mData= new ArrayList<HashMap<String, Object>>() ;
					sql="select u.realname, u.uid,u.photo,c.* from senior_project.Ument as c left join senior_project.User as u on c.uid = u.uid  where time>'"+mData.get(0).get("time") +  " '  and u.uid='"+targetUid+"'order by time desc limit 0, 5";
				}
				ResultSet rs=null;
				int i =0;

				rs=ms.execQuery(sql);
				int rowCount =0;
				double ii;

				if (rs.last()) {
					rowCount= rs.getRow();
					rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
				}


				while(rs.next())
				{
					HashMap<String, Object> map = null;  
					map = new HashMap<String, Object>(); 
					//String uid=rs.getString("uid");
					map.put("username", rs.getString("realname"));
					String time =rs.getString("time");
					time=time.substring(0,time.toCharArray().length-2);
					map.put("time", time);
					map.put("uid", rs.getString("uid"));
					String content=rs.getString("content");
					map.put("content", content);
					//String image =rs.getString("picture");
					map.put("imgbb", rs.getString("picture"));
					map.put("umentid", rs.getString("mid"));
					map.put("eid",rs.getString("eid"));

					if(rs.getString("photo")==null)
					{

						map.put("photo",rs.getString(""));
					}
					else 
					{
						map.put("photo",rs.getString("photo"));
					}

					if(MODE=="pull")
					{
						mData.add(0,map);
					}
					else
					{
						mData.add(map);
					}
					//rs.getRow();
					i++;
					ii=(float)i/(rowCount+0.0)*100;
					//int rowCount = rs.getRow();
					
				}
				
				
				mHandler.obtainMessage(MSG_2).sendToTarget();
				
				

				

			}
			catch (SQLException e) {

			}
			
			
		}
	}
	
	
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
		public HashMap<String, Object> getItem(int position) {
			// TODO Auto-generated method stub
			return mData.get(position-1);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
						Button addevent;

						ImageView photo;

						//View item=mInflater.inflate(R.layout.item1, null);
						ImageLoader imageLoader=new ImageLoader(context);
						Button comment;
						final Button like;

						if(mData.get(position).get("imgbb").toString().equals("0"))
						{
							convertView = mInflater.inflate(R.layout.item1, null);


							TextView username= (TextView)convertView.findViewById(R.id.username_ument);
							TextView text=(TextView)convertView.findViewById(R.id.text_ument);
							TextView time=(TextView)convertView.findViewById(R.id.time_ument);
							addevent=(Button)convertView.findViewById(R.id.add_event1);
							comment=(Button)convertView.findViewById(R.id.umentcomment1);
							like=(Button)convertView.findViewById(R.id.like1);

							comment.setText(comment.getText().toString()+"  "+mData.get(position).get("comment").toString());
							like.setText(like.getText().toString()+"  "+mData.get(position).get("like").toString());

							photo=(ImageView)convertView.findViewById(R.id.list_image);
							if(mData.get(position).get("like_").equals("1"))
							{
								like.setTextColor(Color.parseColor("#DCDCDC"));
							}
							if(mData.get(position).get("eid")!=null)
							{
								addevent.setVisibility(View.VISIBLE);
							}
							username.setText(mData.get(position).get("username").toString());
							time.setText(mData.get(position).get("time").toString());
							text.setText(mData.get(position).get("content").toString());
							imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("photo").toString(), photo);

						}
						else
						{
							convertView = mInflater.inflate(R.layout.item2, null);

							photo=(ImageView)convertView.findViewById(R.id.list_image2);




							TextView username= (TextView)convertView.findViewById(R.id.username_ument2);
							TextView text=(TextView)convertView.findViewById(R.id.text_ument2);
							TextView time=(TextView)convertView.findViewById(R.id.time_ument2);
							final ImageView im=(ImageView)convertView.findViewById(R.id.imageview_event2);
							addevent=(Button)convertView.findViewById(R.id.add_event2);
							comment=(Button)convertView.findViewById(R.id.umentcomment2);
							comment.setText(comment.getText().toString()+"  "+mData.get(position).get("comment").toString());
							like=(Button)convertView.findViewById(R.id.like2);
							like.setText(like.getText().toString()+"  "+mData.get(position).get("like").toString());
							if(mData.get(position).get("like_").equals("1"))
							{
								like.setTextColor(Color.parseColor("#DCDCDC"));
							}
							if(mData.get(position).get("eid")!=null)
							{
								addevent.setVisibility(View.VISIBLE);
							}

							username.setText(mData.get(position).get("username").toString());
							time.setText(mData.get(position).get("time").toString());
							text.setText(mData.get(position).get("content").toString());
							imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("photo").toString(), photo);
							imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("imgbb").toString(), im);


							im.setOnClickListener(new View.OnClickListener() {

								public void onClick(View v) {
									//jumpregisterScreen();
									Intent intent = new Intent(context, anroidpicActivity.class);
									intent.putExtra("url","http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("imgbb").toString() );
									startActivity(intent);  

								}
							});
						}

						photo.setOnClickListener (new View.OnClickListener() {

							public void onClick(View v) {

								Intent intent = new Intent(context, UserInformationActivity.class);

								intent.putExtra("targetUid", mData.get(position).get("uid").toString());
								intent.putExtra("userId", uid);
								startActivity(intent);

							}
						});

						addevent.setOnClickListener(new View.OnClickListener()
						{

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context, Additem_event.class);
								intent.putExtra("eid", mData.get(position).get("eid").toString());
								intent.putExtra("uid", uid);
								intent.putExtra("type", "add");

								startActivity(intent);  

							}

						});
						like.setOnClickListener(new View.OnClickListener()
						{

							@Override
							public void onClick(View v) {
								if(mData.get(position).get("like_").equals("0"))
								{
									Thread temp2 =new Thread() 
									{ 
										@Override
										public void run()
										{
											try 
											{
												String sql1="Insert senior_project.Ument_like (mid,uid) values ('"+mData.get(position).get("umentid")+"','"+uid+"')";
												ms.execUpdate(sql1);
												int like_int=Integer.parseInt(mData.get(position).get("like").toString())+1;
												String sql2="UPDATE senior_project.Ument SET `likes`='"+like_int+"' WHERE `mid`='"+mData.get(position).get("umentid").toString()+"';";
												ms.execUpdate(sql2);

											}
											catch (SQLException e) 
											{
												e.printStackTrace();
												Log.i("SQL ERROR" , "SQL ERROR : " + e.toString());
												//if(e.)


											}
										}
									};
									temp2.start();
									try
									{
										temp2.join();
									}
									catch (InterruptedException e)  
									{  
										e.printStackTrace();  
									}  
									int like_int=Integer.parseInt(mData.get(position).get("like").toString())+1;
									mData.get(position).put("like", like_int+"");
									mData.get(position).put("like_", "1");
									like.setTextColor(Color.parseColor("#DCDCDC"));
									like.setText("Like  "+mData.get(position).get("like").toString());




								}

								else
								{


									Thread temp2 =new Thread() 
									{ 
										@Override
										public void run()
										{
											try 
											{
												String sql1="Delete FROM senior_project.Ument_like where mid='"+mData.get(position).get("umentid").toString()+"' and uid='"+uid+"'";
												ms.execUpdate(sql1);
												int like_int=Integer.parseInt(mData.get(position).get("like").toString())-1;
												String sql2="UPDATE senior_project.Ument SET `likes`='"+like_int+"' WHERE `mid`='"+mData.get(position).get("umentid").toString()+"';";
												ms.execUpdate(sql2);

											}
											catch (SQLException e) 
											{
												e.printStackTrace();
												Log.i("SQL ERROR" , "SQL ERROR : " + e.toString());
												//if(e.)


											}
										}
									};
									temp2.start();
									try
									{
										temp2.join();
									}
									catch (InterruptedException e)  
									{  
										e.printStackTrace();  
									}  
									int like_int=Integer.parseInt(mData.get(position).get("like").toString())-1;
									mData.get(position).put("like", like_int+"");
									like.setTextColor(Color.parseColor("#000000"));
									like.setText("Like  "+mData.get(position).get("like").toString());
									mData.get(position).put("like_", "0");


								}

							}

						});


						comment.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context, Reply.class);
								intent.putExtra("umentid",mData.get(position).get("umentid").toString());
								intent.putExtra("uid",uid);
								intent.putExtra("casenumber","0");
								intent.putExtra("mode","1");
								intent.putExtra("comment",mData.get(position).get("comment").toString());
								startActivityForResult(intent, 0);


							}

						});

						return convertView;  
					}

	}
	
	
	
	

}
