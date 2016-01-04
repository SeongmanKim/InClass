/**
 * Author: Yu Shao
 * Detail: Activity of add comment for thr ument 
 * last version:  3/13/2015
 */
package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
 * 
 * Author: Yu Shao
 * Build the new class, and it extends Activity
 *  
 */
public class Addcommentforument  extends Activity{
	mysql ms;
	String uid;
	String comment;
	String umentid;
	String reply_uid;
	String reply_name;
	Context context;
	EditText comment_view;
	Button back;
	Button send;
	String casenum;
	
	/*
	 * Author: Yu Shao
	 * OnCreate function for build new Activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addcommentforument);
		context=this;
		ms= new mysql();
		Intent intent=getIntent(); 
		umentid=intent.getStringExtra("umentid");
		uid=intent.getStringExtra("uid");
		send=(Button)findViewById(R.id.addcommentsend_addument);
		back=(Button)findViewById(R.id.addcommentback_addument);
		comment_view=(EditText)findViewById(R.id.addcommenttextView_addument);
		if(intent.getStringExtra("reply_uid")!=null)
		{
			reply_uid=intent.getStringExtra("reply_uid");
			reply_name=intent.getStringExtra("reply_name");
			comment_view.setHint("@"+reply_name);
		}
		// set the back button listener
		back.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				finish();
				
			}
			
		});
		// set the send button listner
		send.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				comment=comment_view.getText().toString();
				if(comment.equals(""))
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);  
			         
			        builder.setTitle("Error");  
			        builder.setMessage("You can not send empty comment.!");  
			        builder.setPositiveButton("OK", null);
			        builder.show();
					
				}
				else
				{
				new MyTask().execute("");
				}
				
			}
			
		});
		
 
	}
	
	/**
	 * MyTask class that is extends AsyncTask.
	 * 
	 * Detail: The task is for send comment to mysql. 
	 * @author yushao
	 *
	 */
	private class MyTask extends AsyncTask<String, Integer, String> {
		
		
		ProgressDialog progressDialog= new ProgressDialog(context);
		/*
		 * set the Ui before send comment
		 */
		@Override  
		protected void onPreExecute() {  
			//Log.i(TAG, "onPreExecute() called");  
			progressDialog.setTitle("Sending");
			progressDialog.show();
		}

		/*
		 * insert the query to the sql
		 */
		@Override
		protected String doInBackground(String... params) {
			ms.connectDB();
			
			try {
				
				String sql="";
				if(reply_uid==null)
				{
				sql="INSERT INTO `senior_project`.`Ument_reply` (mid,uid,content) VALUES ('"+umentid+"','"+uid+"','"+comment+"')";
				}
				else
				{
					sql="INSERT INTO `senior_project`.`Ument_reply` (mid,uid,content,reply_uid) VALUES ('"+umentid+"','"+uid+"','"+comment+"','"+reply_uid+"')";
				}
				ms.execUpdate(sql);

			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";


		}
		
		/* 
		 * it is for after send. updata the ui
		 *(non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override  
		protected void onPostExecute(String result) 
		{  
		
			progressDialog.dismiss();

				
				Intent intent = new Intent(Addcommentforument.this,Reply.class);
         
                setResult(RESULT_OK, intent);  
				finish();
			
		}

	}

}
