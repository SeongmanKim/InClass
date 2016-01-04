

/**
 * Author:Yu Shao Young soo. 
 * add event to ument activity.
 * last version: 03/07 
 */


package com.example.inclass;

import java.sql.SQLException;

import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
/*
 * Build the add_event page.
 */
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;
/*
 * 
 * @author yushao
 * 
 * build the new class (Addument_event)that extends Activity 
 *
 */
public class Addument_event extends Activity {
	String eventDay;
	Context context;
	DatePicker dp;
	TimePicker tp;
	String type="1";
	EditText ename;
	EditText stime;
	EditText etime ;
	EditText sday;
	EditText eday;
	EditText loc;
	Button back;
	String uid;
	String D;
	
	//String uid;
/*
 * create the Ui and set information for activity. 
 * (non-Javadoc)
 * @see android.app.Activity#onCreate(android.os.Bundle)
 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addevent);
		
		Button addButton = (Button)findViewById(R.id.add_event);
		ename = (EditText)findViewById(R.id.eventName);
		stime = (EditText)findViewById(R.id.startTime);
		etime = (EditText)findViewById(R.id.endTime);
		sday = (EditText)findViewById(R.id.startDay);
		eday = (EditText)findViewById(R.id.endDay);
		loc = (EditText)findViewById(R.id.location);
		back=(Button)findViewById(R.id.addevent_back);
		context=this;
		Intent intent=getIntent(); 
		eventDay="";
		


		final CheckBox checkS = (CheckBox) findViewById(R.id.check1);
		final CheckBox checkM = (CheckBox) findViewById(R.id.check2);
		final CheckBox checkT = (CheckBox) findViewById(R.id.check3);
		final CheckBox checkW = (CheckBox) findViewById(R.id.check4);
		final CheckBox checkH = (CheckBox) findViewById(R.id.check5);
		final CheckBox checkF = (CheckBox) findViewById(R.id.check6);
		final CheckBox checkA = (CheckBox) findViewById(R.id.check7);

		if(intent.getStringExtra("uid")!=null)
		{
			
			type="2";
			uid=intent.getStringExtra("uid");
			if(intent.getStringExtra("StartTime")!=null)
			{
			ename.setHint("Enter the event name");
			loc.setHint("Enter the location");
			stime.setText(intent.getStringExtra("StartTime"));
			etime.setText(intent.getStringExtra("EndTime"));
			sday.setText(intent.getStringExtra("format"));
			eday.setText(intent.getStringExtra("format"));
			D=intent.getStringExtra("D");
			
			if(D.equals("sunday"))
			{
				checkS.setChecked(true);
				eventDay = eventDay+"S";
			}
			else if(D.equals("monday"))
			{
				checkM.setChecked(true);
				eventDay = eventDay+"M";
		
			}
			else if(D.equals("tuesday"))
			{
				checkT.setChecked(true);
				eventDay = eventDay+"T";
	
			}
			else if(D.equals("wednesday"))
			{
				checkW.setChecked(true);
				eventDay = eventDay+"W";
	
			}
			else if(D.equals("thursday"))
			{
				checkH.setChecked(true);
				eventDay = eventDay+"H";
			
			}
			else if(D.equals("friday"))
			{
				checkF.setChecked(true);
				eventDay = eventDay+"F";
			
			}
			else if(D.equals("saturday"))
			{
				checkA.setChecked(true);
				eventDay = eventDay+"A";
			
			}
			}
			else
			{
				
				ename.setHint("Enter the event name");
				loc.setHint("Enter the location");
				stime.setHint("Enter the start time");
				etime.setHint("Enter the end time");
				sday.setHint("Enter the start date");
				eday.setHint("Enter the end date");
				

			}
			
			
			
		}




		/*
		 * set the add button listener. 
		 */
		back.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) {
			finish();
				
			}
			
		});
		addButton.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) {
				
				if(loc.getText().toString().equals(""))
				{
					loc.setText("UnKnown");
					
				}
				
				if((ename.getText().toString().equals(""))||(stime.getText().toString().equals(""))||(etime.getText().toString().equals(""))||(sday.getText().toString().equals(""))||(eday.getText().toString().equals("")))
				{
					new AlertDialog.Builder(context)  
					.setTitle("Caution")  
					.setMessage("Please fill the all information")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						@Override
						public void onClick(DialogInterface dialoginterface, int i){   

						}   
					}).show();   
				}

				else{
					new MyTask().execute("");
				}
				
			}

		});
		
		checkS.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkS.isChecked())
				{
					eventDay = eventDay+"S";
				}

			}
		});

		checkM.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkM.isChecked())
				{
					eventDay = eventDay+"M";
				}

			}
		});

		checkT.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkT.isChecked())
				{
					eventDay = eventDay+"T";
				}

			}
		});

		checkW.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkW.isChecked())
				{
					eventDay = eventDay+"W";
				}

			}
		});

		checkH.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkH.isChecked())
				{
					eventDay = eventDay+"H";
				}

			}
		});

		checkF.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkF.isChecked())
				{
					eventDay = eventDay+"F";
				}

			}
		});

		checkA.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkA.isChecked())
				{
					eventDay = eventDay+"A";
				}

			}
		});


		stime.setOnClickListener(new EditText.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopup2(Addument_event.this);
			}


		});
		etime.setOnClickListener(new EditText.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopup4(Addument_event.this);
			}


		});

		//		close_c = (Button)findViewById(R.id.close_cal);
		sday.setOnClickListener(new EditText.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showPopup(Addument_event.this);



			}
		});

		//		close_c = (Button)findViewById(R.id.close_cal);
		eday.setOnClickListener(new EditText.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showPopup3(Addument_event.this);



			}
		});


	}




	private void showPopup(final Activity context) {

		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_c);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_calendar, viewGroup);
		dp = (DatePicker) layout.findViewById(R.id.datepickerS);


		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);

		popup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		popup.setFocusable(true);


		// Clear the default translucent background
		//popup.setBackgroundDrawable(new BitmapDrawable());

		// Displaying the popup at the specified location, + offsets.
		popup.showAtLocation(layout, Gravity.CENTER, 0, 0);



		// Getting a reference to Close button, and close the popup when clicked.
		Button close = (Button) layout.findViewById(R.id.close_cal);
		close.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});

		//		   DatePicker dp = (DatePicker) findViewById(R.id.datepickerS);

		Button update = (Button) layout.findViewById(R.id.update_cal);
		update.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean lessthanten = false;
				boolean lessthanten2 = false;
				if(dp.getMonth()<10)
				{
					lessthanten = true;
				}

				if(dp.getDayOfMonth()<10)
				{
					lessthanten2 = true;
				}


				int Month = dp.getMonth()+1;
				int Day = dp.getDayOfMonth();
				int Year = dp.getYear();
				String Month2 = String.valueOf(Month);
				if(lessthanten)
				{
					Month2 = "0"+Month2;
				}
				String Day2 = String.valueOf(Day);
				if(lessthanten2)
				{
					Day2 = "0"+Day2;
				}


				String Year2 = String.valueOf(Year);


				String finals = Month2+Day2+Year2;

				final EditText startdate = (EditText)findViewById(R.id.startDay);
				startdate.setText(finals);
				popup.dismiss();
			}
		});
	}

	private void showPopup3(final Activity context) {

		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_c);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_calendar, viewGroup);
		dp = (DatePicker) layout.findViewById(R.id.datepickerS);


		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);

		popup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		popup.setFocusable(true);


		// Clear the default translucent background
		//popup.setBackgroundDrawable(new BitmapDrawable());

		// Displaying the popup at the specified location, + offsets.
		popup.showAtLocation(layout, Gravity.CENTER, 0, 0);



		// Getting a reference to Close button, and close the popup when clicked.
		Button close = (Button) layout.findViewById(R.id.close_cal);
		close.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});

		//		   DatePicker dp = (DatePicker) findViewById(R.id.datepickerS);

		Button update = (Button) layout.findViewById(R.id.update_cal);
		update.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean lessthanten = false;
				boolean lessthanten2 = false;
				if(dp.getMonth()<10)
				{
					lessthanten = true;
				}
				int Month = dp.getMonth()+1;
				if(dp.getDayOfMonth()<10)
				{
					lessthanten2 = true;
				}
				int Day = dp.getDayOfMonth();
				int Year = dp.getYear();
				String Month2 = String.valueOf(Month);
				if(lessthanten)
				{
					Month2 = "0"+Month2;
				}
				String Day2 = String.valueOf(Day);
				if(lessthanten2)
				{
					Day2 = "0"+Day2;
				}
				String Year2 = String.valueOf(Year);

				String finals = Month2+Day2+Year2;

				final EditText startdate = (EditText)findViewById(R.id.endDay);
				startdate.setText(finals);
				popup.dismiss();
			}
		});
	}


	private void showPopup4(final Activity context)
	{

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_time, null);
		tp = (TimePicker) layout.findViewById(R.id.timePicker1);


		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);

		popup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		popup.setFocusable(true);


		// Clear the default translucent background
		//popup.setBackgroundDrawable(new BitmapDrawable());

		// Displaying the popup at the specified location, + offsets.
		popup.showAtLocation(layout, Gravity.CENTER, 0, 0);



		// Getting a reference to Close button, and close the popup when clicked.
		Button close = (Button) layout.findViewById(R.id.close_time);
		close.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});

		//		   DatePicker dp = (DatePicker) findViewById(R.id.datepickerS);

		Button update = (Button) layout.findViewById(R.id.update_time);
		update.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				String AMPM = "A";
				boolean lessthanten = false;
				int Hour = tp.getCurrentHour();

				if(Hour > 12)
				{
					Hour = Hour - 12;
					AMPM = "P";
				}
				else
				{

					AMPM = "A";

					if(Hour == 12)
					{
						AMPM = "P";
					}
					if(Hour == 0)
					{
						Hour = 12;
						AMPM = "A";
					}
				}
				int min = tp.getCurrentMinute();
				if(min <10)
				{
					lessthanten = true;
				}

				String Hour2 = String.valueOf(Hour);
				if(!Hour2.equals("10")&&!Hour2.equals("11")&&!Hour2.equals("12"))
				{
					Hour2 = "0"+Hour2;
				}
				String min2 = String.valueOf(min);
				//				if(min2.equals("0"))
				//				{
				//					min2 = "0"+min2;
				//				}
				if(lessthanten)
				{
					min2 = "0"+min2;
				}

				String finals = Hour2 +":"+ min2 + AMPM;

				final EditText startTime = (EditText)findViewById(R.id.endTime);
				startTime.setText(finals);
				popup.dismiss();
			}
		});
	}

	private void showPopup2(final Activity context)
	{

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_time, null);
		tp = (TimePicker) layout.findViewById(R.id.timePicker1);


		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);

		popup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		popup.setFocusable(true);


		// Clear the default translucent background
		//popup.setBackgroundDrawable(new BitmapDrawable());

		// Displaying the popup at the specified location, + offsets.
		popup.showAtLocation(layout, Gravity.CENTER, 0, 0);



		// Getting a reference to Close button, and close the popup when clicked.
		Button close = (Button) layout.findViewById(R.id.close_time);
		close.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});

		//		   DatePicker dp = (DatePicker) findViewById(R.id.datepickerS);

		Button update = (Button) layout.findViewById(R.id.update_time);
		update.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				String AMPM = "A";
				boolean lessthanten = false;

				int Hour = tp.getCurrentHour();

				if(Hour > 12)
				{
					Hour = Hour - 12;
					AMPM = "P";
				}
				else
				{

					AMPM = "A";

					if(Hour == 12)
					{
						AMPM = "P";
					}
					if(Hour == 0)
					{
						Hour = 12;
						AMPM = "A";
					}
				}
				int min = tp.getCurrentMinute();
				if(min <10)
				{
					lessthanten = true;
				}
				String Hour2 = String.valueOf(Hour);
				if(!Hour2.equals("10")&&!Hour2.equals("11")&&!Hour2.equals("12"))
				{
					Hour2 = "0"+Hour2;
				}
				String min2 = String.valueOf(min);

				//				if(min2.equals("0"))
				//				{
				//					min2 = "0"+min2;
				//				}
				if(lessthanten)
				{
					min2 = "0"+min2;
				}

				String finals = Hour2 +":"+ min2 + AMPM;

				final EditText startTime = (EditText)findViewById(R.id.startTime);
				startTime.setText(finals);
				popup.dismiss();
			}
		});
	}


	private class MyTask extends AsyncTask<String, Integer, String> {
		ProgressDialog pd;
		protected void onPreExecute() {  
			pd= new ProgressDialog(context);
			pd.setTitle("Updata");
			pd.show();
		}  


		@Override
		protected String doInBackground(String... params) {
			String eid="";
			mysql ms = new mysql();
			ms.connectDB();

				
				String sql="INSERT INTO senior_project.Event (name, day, stime, etime, sdate, edate, location) "
						+ "VALUES ('"+ename.getText().toString().toUpperCase()+"','"+eventDay+"','"+stime.getText().toString()+"','"+etime.getText().toString()+"','"+sday.getText().toString()+"','"+eday.getText().toString()+"','"+loc.getText().toString()+"')";
				
				
				try {
					ms.execUpdate(sql);
					sql="SELECT LAST_INSERT_ID();";
					ResultSet rs=ms.execQuery(sql);
				//	rs=ms.execQuery(sqll);
					while(rs.next())
					{
					String s = rs.getString("LAST_INSERT_ID()");
				
					eid=s;
					}
					
					if(type.equals("2"))
					{
						sql="INSERT INTO senior_project.Event_taking (eid, uid) VALUES ('"+eid+"','"+uid+"')";
						ms.execUpdate(sql);
					}
					
					
					
				}
				
				
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			

			
			
			
			

			
			

			return eid;

		}
		
		protected void onPostExecute(String result) {  
			
			if(type.equals("2"))
			{
				Intent intent = new Intent(Addument_event.this,InClass.class);
		       
	         
					setResult(RESULT_OK, intent); 
					pd.dismiss();
					finish();
			}
			else
			{
			Intent intent = new Intent(Addument_event.this,addument.class);
		        intent.putExtra("eid", result); 
	         
            setResult(RESULT_OK, intent); 
            pd.dismiss();
			finish();
			}



		}  

	}
	
	

	
	
	

}
