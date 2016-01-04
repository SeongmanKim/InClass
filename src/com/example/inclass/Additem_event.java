/**
 * 
 * @auhtor yushao
 * Detail: this is for add new event(new event)
 * last version: 3/13/2015
 */
package com.example.inclass;

import java.sql.SQLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
/*
 * 
 * @author yushao
 * 
 * build the new class that extends Activity 
 *
 */
public class Additem_event extends Activity {
	String eid="";
	String uid="";
	TextView name;
	TextView Data;
	TextView Day;
	TextView location;
	TextView time;
	Context context;
	Button event_add;
	mysql ms;
	String type;

	Button back;
	/*
	 * OnCreate function for build new Activity
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eventinfor);
		Intent intent=getIntent(); 
		context=this;
		ms= new mysql();
		eid=intent.getStringExtra("eid");
		uid=intent.getStringExtra("uid");
		type=intent.getStringExtra("type");
		name = (TextView)findViewById(R.id.eventname);
		Data=(TextView)findViewById(R.id.eventDate);

		Day=(TextView)findViewById(R.id.eventDay);
		location=(TextView)findViewById(R.id.eventLocation);
		time=(TextView)findViewById(R.id.eventTime);
		event_add=(Button)findViewById(R.id.eventdelete);
		back=(Button)findViewById(R.id.event_info_back);
		if(type.equals("add"))
		{
			event_add.setText("Add To My Scheduler");
			event_add.setTextColor(Color.parseColor("#696969"));
		}
		new getinfo().execute("");
		
		/*
		 * set the back button listener
		 */

		back.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) {
				finish();

			}

		});
		/*
		 *  set the event_add button listener
		 */
		event_add.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) {

				new add().execute("");

			}

		});



	}

	/**
	 * 
	 * @author yushao
	 * add class that is extends AsyncTask.
	 * 
	 * Detail: The task is for add new event to mysql. 
	 */
	private class add extends AsyncTask<String, Integer, String> {
		/*
		 *send the new event query to mysql  params="add"
		 *or detail the event query to mydql 
		 */
		@Override
		protected String doInBackground(String... params) {
			String sql;
			if(type.equals("add"))
			{
				sql="INSERT INTO senior_project.Event_taking (eid, uid) VALUES ('"+eid+"','"+uid+"')";
			}
			else
			{
				sql="DELETE FROM senior_project.Event_taking where uid='" + uid + "' and eid='"+eid+"'";
			}
			try {
				ms.execUpdate(sql);

				return "yes";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return "no";
			}



		}
		/*
		 * updata ui. then finish the aqueduct. jump last activity. 
		 * finish();
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(String result) {
			if(type.equals("add"))
			{
				if(result=="yes")
				{
					new AlertDialog.Builder(context)  

					.setMessage("Add Succesful")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						@Override
						public void onClick(DialogInterface dialoginterface, int i){   
							finish();

						}   
					}).show();   

				}
				else
				{

					new AlertDialog.Builder(context)  

					.setMessage("You already have that event")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						@Override
						public void onClick(DialogInterface dialoginterface, int i){   
							finish();

						}   
					}).show(); 
				}

			}
			else
			{
				new AlertDialog.Builder(context)  

				.setMessage("You already delete the event")   
				.setPositiveButton("Ok",   
						new DialogInterface.OnClickListener(){  
					@Override
					public void onClick(DialogInterface dialoginterface, int i){   
						finish();

					}   
				}).show();
				Intent intent = new Intent(context,InClass.class);

				setResult(RESULT_OK, intent);  
				
				finish();

			}

			try {
				ms.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



			//finish();


		}


	}
	
	/**
	 * getinfo is extends asynctask.
	 * it is for get event detail.
	 * @author yushao
	 *
	 */
	
	private class getinfo extends AsyncTask<String, Integer, String> {

		ProgressDialog pd;
		EventInformationPage eventInformationPage;
		/*
		 * set the ProgressDialog show.
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		protected void onPreExecute() {  
			pd= new ProgressDialog(context);
			pd.setTitle("Updata");
			pd.show();
		}  
		
		/*
		 * use the query get the deatil
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */

		@Override
		protected String doInBackground(String... params) {

			ms.connectDB();
			try {
				eventInformationPage= new EventInformationPage(context, ms, ""+eid);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return "";
		}
		/*
		 * add the detail to the Ui. update it.
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */

		protected void onPostExecute(String result) {  




			name.setText(eventInformationPage.returneventinfo().getName());
			
			String temp1=eventInformationPage.returneventinfo().getStartDay();
			String format1=temp1.substring(0,2)+"/"+temp1.substring(2,4)+"/"+temp1.substring(4,8);
			
			
			String temp2=eventInformationPage.returneventinfo().getEndDay();
			String format2=temp2.substring(0,2)+"/"+temp2.substring(2,4)+"/"+temp2.substring(4,8);
			
			if(eventInformationPage.returneventinfo().getStartDay().equals(eventInformationPage.returneventinfo().getEndDay()))
			{
				Data.setText("Data: "+format1);
			}
			else
			{

			Data.setText("Date: "+format1+" -- "+format2);
			}

			Day.setText("Day: "+eventInformationPage.returneventinfo().getDay());

			location.setText("Location: "+eventInformationPage.returneventinfo().getLocation());
			


			time.setText("Time: "+eventInformationPage.returneventinfo().getStartTime()+"-"+eventInformationPage.returneventinfo().getEndTime());
			
			pd.dismiss();



		}  


	}

}
