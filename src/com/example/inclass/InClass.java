/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * @author yushao Young Soo Choi  Seongman Kim
 * 
 * Main Activity, In Class
 */

package com.example.inclass;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


import com.devsmart.android.ui.*;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mysql.jdbc.ResultSet;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class InClass extends Activity {

	ListView listView = null;
	//private static final ListView lvcomment = null;

	//Views for xmls.
	View settingViewForXML;
	View informationPageForXML;
	View eventInformationPageForXML;
	View chattingForXML;
	View schedulerForXML;
	View umentview;
	TextView date;


	// final change
	ArrayList<String> newMessages;



	float eventX = 0;
	float eventY = 0;
	float globalx = 0;
	float globaly = 0;

	int scrolly = 0;

	int eventStartHour = 0;
	int eventStartMin = 0;
	int eventEndHour = 0;
	int eventEndMin = 0;

	float x2 = 0;
	float y2 =0;

	boolean sunday = false;
	boolean monday = false;
	boolean tuesday = false;
	boolean wednesday = false;
	boolean thursday = false;
	boolean friday = false;
	boolean saturday = false;

	//	Button close_c;


	//FOR CLASSMATES
	HorizontalListView studentImageListForXML;
	StudentImageAdapter_forStudentList_taking studentsImageAdapter;
	ArrayList<ArrayList<String>> students_list_in_class;

	//All events
	ArrayList<EventInfo> allEvents;

	int topSchedule = 0;

	Button btn;
	Button btn2;
	Point p;
	DatePicker dp;
	TimePicker tp;

	// Scheduler information.
	SchedulerView schedulerView;
	EventView eventView;

	PopupWindow popup;

	//class information Page
	ClassInformationPage classInformationPage;

	EventInformationPage eventInformationPage;

	//content_layout
	LinearLayout content_layout;

	Intent i;

	long time;


	CountDownLatch latch;
	Taking_studentSide ts;
	TakingE_studentSide es;
	int classInformationNumber;
	int eventInformationNumber;
	// going to class add page
	Button addb;
	Button logoutb;

	Calendar cal = Calendar.getInstance();
	Calendar cal2 = Calendar.getInstance();
	Calendar cal3 = Calendar.getInstance();

	Calendar c =null;
	Calendar c2 =null;
	Calendar c3=Calendar.getInstance();

	String eventDay;


	boolean addClicked;
	private static int RESULT_LOAD_IMAGE = 1;
	public final static int REQUEST_CODE_TAKE_PICTURE = 2;


	ImageView im_;

	setting s;


	//AddClassActivity addclass;
	String Uid;
	String cid;
	String eid;
	String Password;
	String username;
	int casenum=0;
	int addcasenm=0;
	boolean first=true;

	int activated;
	boolean close = false;
	Ument um;

	Display display;


	ChatList chat_list;

	//Connection conn;
	mysql ms;

	int ii;
	TextView tv;
	Context context;

	String picturePath ="";

	List<HashMap<String, Object>> mData; 
	Uri outPutFile;

	TextView setting_name;
	TextView setting_email;

	RadioGroup radgroup;

	private MsgReceiver msgReceiver; 
	RelativeLayout myument;
	ChatList_Adapter adapter;
	Button umentsort;

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		context = this;


		popup = new PopupWindow(this);
		//		close_c = (Button)findViewById(R.id.close_cal);
		settingViewForXML = new View(this);
		//chattingForXML = new View(this);
		classInformationNumber = -1;
		eventInformationNumber = -1;
		Uid="";
		Password="";
		cid="";
		eid="";
		eventDay="";
		tv = new TextView(this);
		ii=1;
		schedulerView = new SchedulerView (this);
		addClicked = false;
		display = getWindowManager().getDefaultDisplay();
		p = new Point();

		allEvents = new ArrayList<EventInfo>();

		// final change
		newMessages = new ArrayList<String>();


		p.x = display.getWidth()/2;
		p.y = display.getHeight()/2;
		latch = new CountDownLatch(1);
		informationPageForXML = View.inflate(this, R.layout.classinfor, null);
		eventInformationPageForXML = View.inflate(this, R.layout.eventinfor, null);
		schedulerForXML = new View(this);

		i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage( getBaseContext().getPackageName() );
		//conn=null;
		ms= new mysql();

		mData = new ArrayList<HashMap<String, Object>>();

		msgReceiver = new MsgReceiver();  
		IntentFilter intentFilter = new IntentFilter();  
		intentFilter.addAction("com.example.inclass.RECEIVER");  
		registerReceiver(msgReceiver, intentFilter);

		if(isOpenNetwork()==false)
		{
			new AlertDialog.Builder(InClass.this)  
			.setTitle("Caution")  
			.setMessage("No Network connection")   
			.setPositiveButton("Ok",   
					new DialogInterface.OnClickListener(){  
				@Override
				public void onClick(DialogInterface dialoginterface, int i){   
				}   
			}).show();   
			jumplogin("1");
		}

		else
		{
			if(SaveSharedPreference.getUserName(this).length() == 0)
			{
				// call Login Activity
				Log.i("AutoLogin", "Not Yet");
				setContentView(R.layout.login);
				//Uid="";

				Button login_button=(Button)findViewById(R.id.btnLogin);
				TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

				new Thread() { 
					@Override
					public void run() {
						ms.connectDB();
						latch.countDown();
					} 
				}.start();

				try {
					latch.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				login_button.setOnClickListener(new Button.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{



						Thread tthread = new checkThread(); 
						EditText ut=(EditText)findViewById(R.id.uid);
						EditText pt=(EditText)findViewById(R.id.password);
						username=ut.getText().toString();

						Password=pt.getText().toString();

						tthread.start();
						try
						{
							tthread.join();  
						}  
						catch (InterruptedException e)  
						{  
							e.printStackTrace();  
						}  
						if(((checkThread) tthread).rcheck()==true)
						{
							setUsername(username);
							InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 
							imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							topSchedule = 0;
							jumpScheduler();

						}
						else 
						{
							Toast.makeText(getApplicationContext(),"please check uid or password",Toast.LENGTH_SHORT).show();
						}

					}
				});

				registerScreen.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						jumpregisterScreen();
					}
				});
			}
			else
			{


				new Thread() { 
					@Override
					public void run() {

						ms.connectDB();

						latch.countDown();
					} 
				}.start();

				try {
					latch.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				Log.i("AutoLogin", "Auto_login with name  " 
						+ SaveSharedPreference.getUserUid(this));
				username = SaveSharedPreference.getUserName(this);
				Uid = SaveSharedPreference.getUserUid(this);
				topSchedule = 0;
				jumpScheduler();

			}
		}


	}


	/*
	 * MsgRECEIVER for broadcastReceiver 
	 * Receive the message from service. 
	 */
	public class MsgReceiver extends BroadcastReceiver{  

		@Override  
		public void onReceive(Context context, Intent intent) {  
			String msg = intent.getStringExtra("msg");
			Chathistory ch = new Chathistory(Uid);
			ChatListWriter chatListWriter;

			if(msg.startsWith("D")||msg.startsWith("MESSAGE"))
			{
				if(msg.startsWith("D"))
				{
					String toid="";
					String _msg = "";
					String[] temp = msg.split(" ", 4);
					toid = temp[2];
					_msg = temp[3];


					ch.writemessgae(toid, _msg,"2");
					chatListWriter = new ChatListWriter(Uid,toid );
					Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

				}


				if(msg.startsWith("MESSAGE"))
				{
					String toid="";
					String _msg = "";
					String[] temp = msg.split(" ", 3);
					toid = temp[1];
					_msg = temp[2];


					ch.writemessgae(toid, _msg,"2");
					chatListWriter = new ChatListWriter(Uid,toid );
					Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

					//final change
					newMessages.add(toid);
				}

				if(activated==2)
				{
					try {
						chat_list = new ChatList(Uid,ms);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					setChatList();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
			}

		}  

	}  






	//set the username
	private void setUsername(String _username) {
		// TODO Auto-generated method stub
		username = _username;
	}

	// check the username and password Thread
	public class checkThread extends Thread  
	{
		boolean check=false;
		@Override
		public void run()  
		{  
			try  
			{  
				try {
					ResultSet rs=null;
					String sql="SELECT uid, password FROM senior_project.User where username='"+username+"'";
					rs=ms.execQuery(sql);
					while(rs.next())
					{
						if(Password.equals(rs.getObject("password")))
						{
							//int uuu;
							Uid=""+(rs.getObject("uid"));
							//uuu=Uid;
							check=true;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}  
			catch (Exception e)  
			{  
				e.printStackTrace();  
			}  
		}  

		public boolean rcheck()
		{
			return check;
		}
	}  
	/*
	 *registerScreen Ui
	 */
	public void jumpregisterScreen()
	{



		setContentView(R.layout.register);
		TextView logins = (TextView) findViewById(R.id.link_to_login);
		final TextView username=(TextView) findViewById(R.id.username);
		final TextView password=(TextView) findViewById(R.id.reg_password);
		final TextView email=(TextView)findViewById(R.id.Email);
		final TextView fullname=(TextView)findViewById(R.id.reg_fullname);
		Button re= (Button)findViewById(R.id.btnRegister);
		re.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 

				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
				if(username.getText().toString().equals(""))
				{
					new AlertDialog.Builder(InClass.this)  
					.setTitle("Caution")  
					.setMessage("Please enter the username")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						@Override
						public void onClick(DialogInterface dialoginterface, int i){   

						}   
					}).show();   
				}
				else if(password.getText().toString().equals(""))
				{
					new AlertDialog.Builder(InClass.this)  
					.setTitle("Caution")  
					.setMessage("Please enter the password")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						@Override
						public void onClick(DialogInterface dialoginterface, int i){     
						}   
					}).show(); 

				}
				else if(email.getText().toString().equals(""))
				{
					new AlertDialog.Builder(InClass.this)  
					.setTitle("Caution")  
					.setMessage("Please enter the E-mail")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						@Override
						public void onClick(DialogInterface dialoginterface, int i){   

						}   
					}).show(); 

				}
				else if(fullname.getText().toString().equals(""))
				{
					new AlertDialog.Builder(InClass.this)  
					.setTitle("Caution")  
					.setMessage("Please enter the Fullname")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						@Override
						public void onClick(DialogInterface dialoginterface, int i){   

						}   
					}).show(); 

				}



				else
				{
					casenum=0;
					//final int casenum=0;
					Thread temp =new Thread() 
					{ 

						String sql="INSERT INTO `senior_project`.`User` (`username`, `password`, `realname`, `gender`, `birthday`, `umail`) VALUES ('"+username.getText().toString()+"', '"+password.getText().toString()+"','"+fullname.getText().toString()+"','M','19920101','"+email.getText()+"')";
						//ResultSet rs=null;

						//rs=ms.execQuery(sql);



						@Override
						public void run()
						{

							try 
							{
								//Log.i("sql",sql);
								ms.execUpdate(sql);


								sql="SELECT LAST_INSERT_ID();";
								ResultSet rs=ms.execQuery(sql);

								while(rs.next())
								{
									String s=rs.getString("LAST_INSERT_ID()");
									Uid=s;
								}



							}
							catch (SQLException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.i("infor",e.toString());
								if(e.toString().indexOf("umail")!=-1)
								{

									casenum=1;
								}
								else 
								{
									casenum=2;
								}

							}

						}


					};
					temp.start();
					try
					{
						temp.join();
					}
					catch (InterruptedException e)  
					{  
						e.printStackTrace();  
					}
					if(casenum==0)
					{
						new AlertDialog.Builder(InClass.this)  
						.setTitle("congratulation")  
						.setMessage("congratulation! "+username.getText().toString()+" is register successful")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							@Override
							public void onClick(DialogInterface dialoginterface, int i){   
							}   
						}).show();   
						topSchedule = 0;
						jumpScheduler();
					}
					else if(casenum==1)
					{

						new AlertDialog.Builder(InClass.this)  
						.setTitle("Caution")  
						.setMessage("The E-mail has been register")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							@Override
							public void onClick(DialogInterface dialoginterface, int i){   
								//������������   
								//Toast.makeText(InClass.this, "������",Toast.LENGTH_LONG).show();  
							}   
						}).show();   
					}
					else if(casenum==2)
					{
						new AlertDialog.Builder(InClass.this)  
						.setTitle("Caution")  
						.setMessage("The Username has been register")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							@Override
							public void onClick(DialogInterface dialoginterface, int i){   

							}   
						}).show();  
					}
				}

			}



		});

		// set the login button listener. 
		logins.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				jumplogin("0");
			}
		});




	}

	// jump the login ui
	public void jumplogin(final String i)
	{

		setContentView(R.layout.login);





		Button login_button=(Button)findViewById(R.id.btnLogin);
		TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
		login_button.setOnClickListener(new Button.OnClickListener()
		{




			@Override
			public void onClick(View v)

			{


				if(isOpenNetwork()==false)
				{

					new AlertDialog.Builder(InClass.this)  
					.setTitle("Caution")  
					.setMessage("No Network connection")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						@Override
						public void onClick(DialogInterface dialoginterface, int i){   
						}   
					}).show();   

				}
				else
				{

					if(i.equals("1"))
					{
						new Thread() { 
							@Override
							public void run() {
								ms.connectDB();
								latch.countDown();
							} 
						}.start();
					}
					Thread tthread = new checkThread(); 

					EditText ut=(EditText)findViewById(R.id.uid);
					EditText pt=(EditText)findViewById(R.id.password);
					username=ut.getText().toString();
					Password=pt.getText().toString();

					tthread.start();
					try  
					{  
						tthread.join();  
					}  
					catch (InterruptedException e)  
					{  
						e.printStackTrace();  
					}  

					if(((checkThread) tthread).rcheck()==true)
					{
						topSchedule = 0;
						jumpScheduler();
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 

						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
					}
					else 
					{
						Toast.makeText(getApplicationContext(),"please check uid or password",Toast.LENGTH_SHORT).show();

					}
				}

			}

		});

		registerScreen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpregisterScreen();
			}
		});
	}
	//
	public void jumpAddevent()
	{
		//	eAddClicked = false;
		tv.setText("Add"
				+ " Event");
		ii=2;

		View addEventViewForXML = View.inflate(this, R.layout.addevent, null);
		content_layout.removeAllViews();
		content_layout.addView(addEventViewForXML);

		addb.setVisibility(View.GONE);



		Button addButton = (Button)findViewById(R.id.add_event);
		final EditText ename = (EditText)findViewById(R.id.eventName);
		final EditText stime = (EditText)findViewById(R.id.startTime);
		final EditText etime = (EditText)findViewById(R.id.endTime);
		final EditText sday = (EditText)findViewById(R.id.startDay);
		final EditText eday = (EditText)findViewById(R.id.endDay);
		final EditText loc = (EditText)findViewById(R.id.location);

		ename.setHint("Enter the event name.");


		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		today.month = today.month+1;
		if((today.month)<10)
		{
			if(today.monthDay<10)
			{
				sday.setHint(0+""+today.month+""+0+""+today.monthDay+""+today.year+"");
				eday.setHint(0+""+today.month+""+0+""+today.monthDay+""+today.year+"");
			}
			else
			{
				sday.setHint(0+""+today.month+""+today.monthDay+""+today.year+"");
				eday.setHint(0+""+today.month+""+today.monthDay+""+today.year+"");
			}
		}
		else
		{
			if(today.monthDay<10)
			{
				sday.setHint(today.month+""+0+""+today.monthDay+""+today.year+"");
				eday.setHint(today.month+""+0+""+today.monthDay+""+today.year+"");
			}
			else
			{
				sday.setHint(today.month+""+today.monthDay+""+today.year+"");
				eday.setHint(today.month+""+today.monthDay+""+today.year+"");
			}
		}

		if(today.hour>12)
		{
			int pmhour = today.hour-12;
			if(pmhour<10)
			{
				if(today.minute<10)
				{
					stime.setHint(0+""+pmhour+""+":"+0+""+today.minute+"P");
				}
				else
				{
					stime.setHint(0+""+pmhour+""+":"+today.minute+"P");
				}			
			}
			else
			{
				if(today.minute<10)
				{
					stime.setHint(pmhour+""+":"+0+""+today.minute+"P");
				}
				else
				{
					stime.setHint(pmhour+""+":"+today.minute+"P");
				}			
			}

		}
		else
		{
			if(today.hour<10)
			{
				if(today.minute<10)
				{
					stime.setHint(0+""+today.hour+""+":"+0+""+today.minute+"A");
				}
				else
				{
					stime.setHint(0+""+today.hour+""+":"+today.minute+"A");
				}			
			}
			else
			{
				if(today.minute<10)
				{
					stime.setHint(today.hour+""+":"+0+""+today.minute+"A");
				}
				else
				{
					stime.setHint(today.hour+""+":"+today.minute+"A");
				}			
			}

		}

		today.hour = today.hour+1;

		if(today.hour>12)
		{
			int pmhour = today.hour-12;
			if(pmhour<10)
			{
				if(today.minute<10)
				{
					etime.setHint(0+""+pmhour+""+":"+0+""+today.minute+"P");
				}
				else
				{
					etime.setHint(0+""+pmhour+""+":"+today.minute+"P");
				}			
			}
			else
			{
				if(today.minute<10)
				{
					etime.setHint(pmhour+""+":"+0+""+today.minute+"P");
				}
				else
				{
					etime.setHint(pmhour+""+":"+today.minute+"P");
				}			
			}

		}
		else
		{
			if(today.hour<10)
			{
				if(today.minute<10)
				{
					etime.setHint(0+""+today.hour+""+":"+0+""+today.minute+"A");
				}
				else
				{
					etime.setHint(0+""+today.hour+""+":"+today.minute+"A");
				}			
			}
			else
			{
				if(today.minute<10)
				{
					etime.setHint(today.hour+""+":"+0+""+today.minute+"A");
				}
				else
				{
					etime.setHint(today.hour+""+":"+today.minute+"A");
				}			
			}

		}

		loc.setHint("Enter the location.");

		eventDay="";

		final CheckBox checkS = (CheckBox) findViewById(R.id.check1);
		final CheckBox checkM = (CheckBox) findViewById(R.id.check2);
		final CheckBox checkT = (CheckBox) findViewById(R.id.check3);
		final CheckBox checkW = (CheckBox) findViewById(R.id.check4);
		final CheckBox checkH = (CheckBox) findViewById(R.id.check5);
		final CheckBox checkF = (CheckBox) findViewById(R.id.check6);
		final CheckBox checkA = (CheckBox) findViewById(R.id.check7);

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
				showPopup2(InClass.this);
			}


		});
		etime.setOnClickListener(new EditText.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopup4(InClass.this);
			}


		});

		//		close_c = (Button)findViewById(R.id.close_cal);
		sday.setOnClickListener(new EditText.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showPopup(InClass.this);



			}
		});

		//		close_c = (Button)findViewById(R.id.close_cal);
		eday.setOnClickListener(new EditText.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showPopup3(InClass.this);



			}
		});

		addButton.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{

				if(eventDay == "")
				{
					Calendar cc = Calendar.getInstance();
					int dayCheck = cc.get(Calendar.DAY_OF_WEEK);
					//Cal week day
					switch(dayCheck)
					{
					case 1:
						eventDay = "S";

						break;
					case 2:
						eventDay = "M";

						break;
					case 3:
						eventDay = "T";

						break;
					case 4:
						eventDay = "W";

						break;
					case 5:
						eventDay = "H";

						break;
					case 6:
						eventDay = "F";

						break;
					case 7:
						eventDay = "A";

						break;
					}
				}

				if(sday.getText().toString().matches(""))
				{
					//stime.sett

					Time today = new Time(Time.getCurrentTimezone());
					today.setToNow();
					today.month = today.month+1;
					if((today.month)<10)
					{
						if(today.monthDay<10)
						{
							sday.setText(0+""+today.month+""+0+""+today.monthDay+""+today.year+"");
						}
						else
						{
							sday.setText(0+""+today.month+""+today.monthDay+""+today.year+"");
						}
					}
					else
					{
						if(today.monthDay<10)
						{
							sday.setText(today.month+""+0+""+today.monthDay+""+today.year+"");
						}
						else
						{
							sday.setText(today.month+""+today.monthDay+""+today.year+"");
						}
					}

					Log.i("set", sday.getText().toString());

				}

				if(eday.getText().toString().matches(""))
				{
					//stime.sett
					eday.setText(sday.getText().toString());					
				}

				if(stime.getText().toString().matches(""))
				{

					Time today = new Time(Time.getCurrentTimezone());
					today.setToNow();


					if(today.hour>12)
					{
						int pmhour = today.hour-12;
						if(pmhour<10)
						{
							if(today.minute<10)
							{
								stime.setText(0+""+pmhour+""+":"+0+""+today.minute+"P");
							}
							else
							{
								stime.setText(0+""+pmhour+""+":"+today.minute+"P");
							}			
						}
						else
						{
							if(today.minute<10)
							{
								stime.setText(pmhour+""+":"+0+""+today.minute+"P");
							}
							else
							{
								stime.setText(pmhour+""+":"+today.minute+"P");
							}			
						}

					}
					else
					{
						if(today.hour<10)
						{
							if(today.minute<10)
							{
								stime.setText(0+""+today.hour+""+":"+0+""+today.minute+"A");
							}
							else
							{
								stime.setText(0+""+today.hour+""+":"+today.minute+"A");
							}			
						}
						else
						{
							if(today.minute<10)
							{
								stime.setText(today.hour+""+":"+0+""+today.minute+"A");
							}
							else
							{
								stime.setText(today.hour+""+":"+today.minute+"A");
							}			
						}

					}

					Log.i("set", stime.getText().toString());

				}

				if(etime.getText().toString().matches(""))
				{

					Time today = new Time(Time.getCurrentTimezone());
					today.setToNow();

					today.hour = today.hour+1;

					if(today.hour>12)
					{
						int pmhour = today.hour-12;
						if(pmhour<10)
						{
							if(today.minute<10)
							{
								etime.setText(0+""+pmhour+""+":"+0+""+today.minute+"P");
							}
							else
							{
								etime.setText(0+""+pmhour+""+":"+today.minute+"P");
							}			
						}
						else
						{
							if(today.minute<10)
							{
								etime.setText(pmhour+""+":"+0+""+today.minute+"P");
							}
							else
							{
								etime.setText(pmhour+""+":"+today.minute+"P");
							}			
						}

					}
					else
					{
						if(today.hour<10)
						{
							if(today.minute<10)
							{
								etime.setText(0+""+today.hour+""+":"+0+""+today.minute+"A");
							}
							else
							{
								etime.setText(0+""+today.hour+""+":"+today.minute+"A");
							}			
						}
						else
						{
							if(today.minute<10)
							{
								etime.setText(today.hour+""+":"+0+""+today.minute+"A");
							}
							else
							{
								etime.setText(today.hour+""+":"+today.minute+"A");
							}			
						}

					}

					Log.i("set", stime.getText().toString());

				}



				if(loc.getText().toString().matches(""))
				{
					loc.setText("UNKOWN");
				}
				if(ename.getText().toString().matches(""))
				{
					ename.setText("UNKOWN");
				}

				final String sql2="INSERT INTO senior_project.Event (name, day, stime, etime, sdate, edate, location) "
						+ "VALUES ('"+ename.getText().toString().toUpperCase()+"','"+eventDay+"','"+stime.getText().toString()+"','"+etime.getText().toString()+"','"+sday.getText().toString()+"','"+eday.getText().toString()+"','"+loc.getText().toString()+"')";

				Thread temp2 =new Thread() 
				{ 
					@Override
					public void run()
					{
						try 
						{
							ms.execUpdate(sql2);

							String sql="SELECT LAST_INSERT_ID();";
							ResultSet rs=ms.execQuery(sql);
							//	rs=ms.execQuery(sqll);
							while(rs.next())
							{
								String s = rs.getString("LAST_INSERT_ID()");
								eid=s;

							}
						}
						catch (SQLException e) 
						{
							e.printStackTrace();
							Log.i("SQL ERROR" , "SQL ERROR : " + e.toString());
							//if(e.)
							addcasenm=1;

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

				///////////////////////////////////////////////////




				//eAddClicked = true;
				final String sql="INSERT INTO senior_project.Event_taking (eid, uid) VALUES ('"+eid+"','"+Uid+"')";

				Thread temp =new Thread() 
				{ 
					@Override
					public void run()
					{
						try 
						{
							ms.execUpdate(sql);
						}
						catch (SQLException e) 
						{
							e.printStackTrace();
							Log.i("SQL ERROR" , "SQL ERROR : " + e.toString());
							//if(e.)
							addcasenm=1;

						}
					}
				};
				temp.start();
				try
				{
					temp.join();
				}
				catch (InterruptedException e)  
				{  
					e.printStackTrace();  
				}  


				///////////////////////////////////////////////////////



				new AlertDialog.Builder(InClass.this)  
				.setTitle("")  
				.setMessage("Add class sucessful")   
				.setPositiveButton("Ok",   
						new DialogInterface.OnClickListener(){  
					@Override
					public void onClick(DialogInterface dialoginterface, int i){   
					}   
				}).show();
				topSchedule = 0;
				jumpScheduler();

			}
			//





		});

	}

	//	public void jumpAddevent2(int _shour, int _smin, int _ehour, int _emin)
	//	{
	//		//	eAddClicked = false;
	//		tv.setText("Add"
	//				+ " Event");
	//		ii=2;
	//
	//		String AMPM = "A";
	//		boolean lessthanten = false;
	//		eventDay="";
	//		View addEventViewForXML = View.inflate(this, R.layout.addevent, null);
	//		content_layout.removeAllViews();
	//		content_layout.addView(addEventViewForXML);
	//
	//		addb.setVisibility(View.GONE);
	//
	//		Button addButton = (Button)findViewById(R.id.add_event);
	//		EditText stDay = (EditText)findViewById(R.id.startDay);
	//		EditText endDay = (EditText)findViewById(R.id.endDay);
	//		EditText stTime = (EditText)findViewById(R.id.startTime);
	//		EditText endTime = (EditText)findViewById(R.id.endTime);
	//
	//		Button update = (Button)findViewById(R.id.update_cal);
	//
	//		final EditText ename = (EditText)findViewById(R.id.eventName);
	//		final EditText stime = (EditText)findViewById(R.id.startTime);
	//		final EditText etime = (EditText)findViewById(R.id.endTime);
	//		final EditText sday = (EditText)findViewById(R.id.startDay);
	//		final EditText eday = (EditText)findViewById(R.id.endDay);
	//		final EditText loc = (EditText)findViewById(R.id.location);
	//		
	//		ename.setHint("Enter the event name");
	//		loc.setHint("Enter the location");
	//
	//		final CheckBox checkS = (CheckBox) findViewById(R.id.check1);
	//		final CheckBox checkM = (CheckBox) findViewById(R.id.check2);
	//		final CheckBox checkT = (CheckBox) findViewById(R.id.check3);
	//		final CheckBox checkW = (CheckBox) findViewById(R.id.check4);
	//		final CheckBox checkH = (CheckBox) findViewById(R.id.check5);
	//		final CheckBox checkF = (CheckBox) findViewById(R.id.check6);
	//		final CheckBox checkA = (CheckBox) findViewById(R.id.check7);
	//		if(sunday)
	//		{
	//			checkS.setChecked(true);
	//			eventDay = eventDay+"S";
	//			sunday = false;
	//		}
	//		else if(monday)
	//		{
	//			checkM.setChecked(true);
	//			eventDay = eventDay+"M";
	//			cal2.add(Calendar.DAY_OF_MONTH, 1);
	//			monday = false;
	//		}
	//		else if(tuesday)
	//		{
	//			checkT.setChecked(true);
	//			eventDay = eventDay+"T";
	//			cal2.add(Calendar.DAY_OF_MONTH, 2);
	//			tuesday = false;
	//		}
	//		else if(wednesday)
	//		{
	//			checkW.setChecked(true);
	//			eventDay = eventDay+"W";
	//			cal2.add(Calendar.DAY_OF_MONTH, 3);
	//			wednesday = false;
	//		}
	//		else if(thursday)
	//		{
	//			checkH.setChecked(true);
	//			eventDay = eventDay+"H";
	//			cal2.add(Calendar.DAY_OF_MONTH, 4);
	//			thursday = false;
	//		}
	//		else if(friday)
	//		{
	//			checkF.setChecked(true);
	//			eventDay = eventDay+"F";
	//			cal2.add(Calendar.DAY_OF_MONTH, 5);
	//			friday = false;
	//		}
	//		else if(saturday)
	//		{
	//			checkA.setChecked(true);
	//			eventDay = eventDay+"A";
	//			cal2.add(Calendar.DAY_OF_MONTH, 6);
	//			saturday = false;
	//		}
	//
	//		String textStartTime = timeConvert(_shour, _smin);
	//		String textEndTime = timeConvert(_ehour, _emin);
	//		stTime.setText(textStartTime);
	//		endTime.setText(textEndTime);
	//		SimpleDateFormat format1 = new SimpleDateFormat("MMddyyyy");
	//		String format = format1.format(cal2.getTime());
	//		sday.setText(format);
	//		eday.setText(format);
	//
	//		checkS.setOnClickListener(new View.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				if(checkS.isChecked())
	//				{
	//					eventDay = eventDay+"S";
	//				}
	//
	//			}
	//		});
	//
	//		checkM.setOnClickListener(new View.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				if(checkM.isChecked())
	//				{
	//					eventDay = eventDay+"M";
	//				}
	//
	//			}
	//		});
	//
	//		checkT.setOnClickListener(new View.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				if(checkT.isChecked())
	//				{
	//					eventDay = eventDay+"T";
	//				}
	//
	//			}
	//		});
	//
	//		checkW.setOnClickListener(new View.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				if(checkW.isChecked())
	//				{
	//					eventDay = eventDay+"W";
	//				}
	//
	//			}
	//		});
	//
	//		checkH.setOnClickListener(new View.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				if(checkH.isChecked())
	//				{
	//					eventDay = eventDay+"H";
	//				}
	//
	//			}
	//		});
	//
	//		checkF.setOnClickListener(new View.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				if(checkF.isChecked())
	//				{
	//					eventDay = eventDay+"F";
	//				}
	//
	//			}
	//		});
	//
	//		checkA.setOnClickListener(new View.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				if(checkA.isChecked())
	//				{
	//					eventDay = eventDay+"A";
	//				}
	//
	//			}
	//		});
	//
	//
	//		stTime.setOnClickListener(new EditText.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				showPopup2(InClass.this);
	//			}
	//
	//
	//		});
	//		endTime.setOnClickListener(new EditText.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				showPopup4(InClass.this);
	//			}
	//
	//
	//		});
	//
	//		//		close_c = (Button)findViewById(R.id.close_cal);
	//		stDay.setOnClickListener(new EditText.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//
	//				showPopup(InClass.this);
	//
	//
	//
	//			}
	//		});
	//
	//		//		close_c = (Button)findViewById(R.id.close_cal);
	//		endDay.setOnClickListener(new EditText.OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//
	//				showPopup3(InClass.this);
	//
	//
	//
	//			}
	//		});
	//
	//		addButton.setOnClickListener(new Button.OnClickListener()
	//		{
	//
	//			@Override
	//			public void onClick(View v) 
	//			{
	//
	//				if(eday.getText().toString().matches(""))
	//				{
	//					eday.setText(sday.getText().toString());
	//				}
	//				if(ename.getText().toString().matches(""))
	//				{
	//					ename.setText("UNKWON");
	//				}
	//
	//				if(loc.getText().toString().matches(""))
	//				{
	//					ename.setText("UNKWON");
	//				}
	//
	//				final String sql2="INSERT INTO senior_project.Event (name, day, stime, etime, sdate, edate, location) "
	//						+ "VALUES ('"+ename.getText().toString().toUpperCase()+"','"+eventDay+"','"+stime.getText().toString()+"','"+etime.getText().toString()+"','"+sday.getText().toString()+"','"+eday.getText().toString()+"','"+loc.getText().toString()+"')";
	//
	//				Thread temp2 =new Thread() 
	//				{ 
	//					@Override
	//					public void run()
	//					{
	//						try 
	//						{
	//							ms.execUpdate(sql2);
	//
	//							String sql="SELECT LAST_INSERT_ID();";
	//							ResultSet rs=ms.execQuery(sql);
	//							//	rs=ms.execQuery(sqll);
	//							while(rs.next())
	//							{
	//								String s = rs.getString("LAST_INSERT_ID()");
	//								eid=s;
	//
	//							}
	//						}
	//						catch (SQLException e) 
	//						{
	//							e.printStackTrace();
	//							Log.i("SQL ERROR" , "SQL ERROR : " + e.toString());
	//							//if(e.)
	//							addcasenm=1;
	//
	//						}
	//					}
	//				};
	//				temp2.start();
	//				try
	//				{
	//					temp2.join();
	//				}
	//				catch (InterruptedException e)  
	//				{  
	//					e.printStackTrace();  
	//				}  
	//
	//				///////////////////////////////////////////////////
	//
	//
	//
	//
	//				//eAddClicked = true;
	//				final String sql="INSERT INTO senior_project.Event_taking (eid, uid) VALUES ('"+eid+"','"+Uid+"')";
	//
	//				Thread temp =new Thread() 
	//				{ 
	//					@Override
	//					public void run()
	//					{
	//						try 
	//						{
	//							ms.execUpdate(sql);
	//						}
	//						catch (SQLException e) 
	//						{
	//							e.printStackTrace();
	//							Log.i("SQL ERROR" , "SQL ERROR : " + e.toString());
	//							//if(e.)
	//							addcasenm=1;
	//
	//						}
	//					}
	//				};
	//				temp.start();
	//				try
	//				{
	//					temp.join();
	//				}
	//				catch (InterruptedException e)  
	//				{  
	//					e.printStackTrace();  
	//				}  
	//
	//
	//				///////////////////////////////////////////////////////
	//
	//
	//
	//				new AlertDialog.Builder(InClass.this)  
	//				.setTitle("")  
	//				.setMessage("Add class sucessful")   
	//				.setPositiveButton("Ok",   
	//						new DialogInterface.OnClickListener(){  
	//					@Override
	//					public void onClick(DialogInterface dialoginterface, int i){   
	//					}   
	//				}).show();
	//				jumpScheduler();
	//
	//			}
	//			//
	//
	//
	//
	//
	//
	//		});
	//
	//	}

	public String timeConvert(int _hour, int _min)
	{
		String AMPM = "A";
		boolean lessthanten = false;

		if(_hour > 12)
		{
			_hour = _hour - 12;
			AMPM = "P";
		}
		else
		{

			AMPM = "A";

			if(_hour == 12)
			{
				AMPM = "P";
			}
			if(_hour == 0)
			{
				_hour = 12;
				AMPM = "A";
			}
		}

		if(_min <10)
		{
			lessthanten = true;
		}
		String Hour2 = String.valueOf(_hour);
		if(!Hour2.equals("10")&&!Hour2.equals("11")&&!Hour2.equals("12"))
		{
			Hour2 = "0"+Hour2;
		}
		String min2 = String.valueOf(_min);

		if(min2.equals("0"))
		{
			min2 = "0"+min2;
		}
		if(lessthanten)
		{
			min2 = "0"+min2;
		}

		return  Hour2 +":00"+ AMPM;
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

	// jump the add class page

	public void jumpAddclass(){
		//final String cid="";
		addClicked = false;
		tv.setText("Add"
				+ " Class");
		ii=2;
		View addClassViewForXML = View.inflate(this, R.layout.addclass, null);
		content_layout.removeAllViews();
		content_layout.addView(addClassViewForXML);
		//
		//setContentView(R.layout.addclass);
		addb.setVisibility(View.GONE);

		Button addButton = (Button)findViewById(R.id.add);
		Button searchButton=(Button)findViewById(R.id.searchbutton);
		//i = getBaseContext().getPackageManager()
		//		.getLaunchIntentForPackage( getBaseContext().getPackageName() );
		final ListView listview = (ListView) findViewById(R.id.listclass);
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HashMap<String,String> map=(HashMap<String,String>)listview.getItemAtPosition(arg2);

				cid=map.get("cid");
			}
		});

		addButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				//Log.i("SQL ERROR" , "SQL ERROR : get in here?");
				addClicked = true;
				if(addClicked){
					final String sql="INSERT INTO senior_project.Taking (`cid`, `uid`) VALUES ('"+cid+"','"+Uid+"')";

					Thread temp =new Thread() 
					{ 
						@Override
						public void run()
						{
							try 
							{
								ms.execUpdate(sql);
							}
							catch (SQLException e) 
							{
								e.printStackTrace();
								Log.i("SQL ERROR" , "SQL ERROR : " + e.toString());
								//if(e.)
								addcasenm=1;

							}
						}
					};
					temp.start();
					try
					{
						temp.join();
					}
					catch (InterruptedException e)  
					{  
						e.printStackTrace();  
					}  

					if(addcasenm==1)
					{
						new AlertDialog.Builder(InClass.this)  
						.setTitle("Caution")  
						.setMessage("You have already added this class")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							@Override
							public void onClick(DialogInterface dialoginterface, int i){   
							}   
						}).show();   
						topSchedule = 0;
						jumpScheduler();
					}
					else 
					{
						new AlertDialog.Builder(InClass.this)  
						.setTitle("")  
						.setMessage("Add class sucessful")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							@Override
							public void onClick(DialogInterface dialoginterface, int i){   
							}   
						}).show();   
						topSchedule = 0;
						jumpScheduler();
					}
				}
				topSchedule = 0;
				jumpScheduler();



			}           

		});


		// set the search button listener. 
		searchButton.setOnClickListener(new Button.OnClickListener(){


			@Override
			public void onClick(View v) {
				//				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 
				//
				//				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  

				final ArrayList<HashMap<String,String>> myArrayList=new ArrayList<HashMap<String,String>>();   


				///   test;;;;


				final EditText ecn= (EditText)findViewById(R.id.classnumber);
				final EditText esbj= (EditText)findViewById(R.id.subject);
				//String sql="SELECT * FROM developer01_test.class where dep=+\""+esbj+ "\"and class_num="+ecn+"\"";

				Thread temp =new Thread()  
				{ 
					@Override
					public void run()
					{
						try {
							ResultSet rs=null;
							String sql="SELECT * FROM senior_project.Course where department like \"%"+esbj.getText().toString().toUpperCase()+ "%\"and number like "+"\"%"+ecn.getText()+"%\"";
							rs=ms.execQuery(sql);
							while(rs.next())
							{
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("cid", rs.getString("cid")+"");

								map.put("name", (String) rs.getObject("cname"));
								int sec=(Integer) rs.getObject("section");
								String secs="";
								if(sec>=10)
								{
									secs="0"+sec+"";
								}
								else 
								{
									secs="00"+sec+"";
								}



								map.put("pro", "Instructor:	"+(String) rs.getObject("professor"));
								map.put("location", "Location:	"+(String) rs.getObject("location"));
								map.put("time", "Time:			"+(String)rs.getObject("stime")+"--"+(String)rs.getObject("etime")+" "+rs.getObject("date")+"");;

								map.put("subject", rs.getString("department")+""+rs.getString("number")+""+" - "+secs);

								myArrayList.add(map); 

							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
				};
				temp.start();
				try
				{
					temp.join();
				}
				catch (InterruptedException e)  
				{  
					e.printStackTrace();  
				}  
				listclass(myArrayList);
			}           
		});
	}
	public void listclass(ArrayList<HashMap<String,String>> myArrayList)
	{
		final ListView listview = (ListView) findViewById(R.id.listclass); 
		SimpleAdapter mySimpleAdapter=new SimpleAdapter(this,   
				myArrayList,  
				R.layout.list_item,
				new String[]{"subject","name","pro","location","time"},  
				new int[]{R.id.name,R.id.subject,R.id.teacher,R.id.location,R.id.time});   

		listview.setAdapter(mySimpleAdapter); 	
	}
	// jump the scheduler page. 
	public void jumpScheduler()
	{
		if(SaveSharedPreference.getUserName(this).length()==0){
			SaveSharedPreference.setUserInfo(this, username, Uid);
		}

		if(first==true)
		{
			Intent serviceIntent = new Intent(this, MsgService.class);
			serviceIntent.putExtra("Uid", Uid);
			startService(serviceIntent);
			first=false;
		}

		/**
		 * TEST
		 */
		//FOR STUDENT IMAGE OF ALL CLASSMATES
		studentsImageAdapter = new StudentImageAdapter_forStudentList_taking(context,Uid);
		students_list_in_class = new ArrayList<ArrayList<String>>();

		Log.i("AutoLogin", "Auto_going to scheduler");
		Log.i("My username", "auto " +username);
		Log.i("My Uid", "auto " + Uid);
		ts=new Taking_studentSide(Uid,username);
		ts.getClasses(ms);

		es = new TakingE_studentSide(Uid, username);
		es.getEvents();

		//logoutb=(Button) findViewById(R.id.logOut);
		schedulerView = new SchedulerView(this);
		schedulerView.addList(ts.getClasses());

		eventView = new EventView(this);
		eventView.addList(es.getEvents());

		ii=3;
		i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage( getBaseContext().getPackageName() );
		setContentView(R.layout.activity_in_class);

		activated = 0;
		tv= (TextView) findViewById(R.id.tt1);
		addb=(Button)findViewById(R.id.addbutton_activity);
		//logoutb=(Button)findViewById(R.id.logOut);
		//tv.setBackgroundColor(Color.RED);
		//tv.setTextColor(Color.WHITE);

		content_layout = (LinearLayout) findViewById(R.id.cont_layer);

		content_layout.removeAllViews();
		content_layout = (LinearLayout) findViewById(R.id.cont_layer);

		schedulerForXML = View.inflate(context, R.layout.calendar, null);
		content_layout.addView(schedulerForXML);

		DrawScheduler();

		final Button button_scheduler = (Button) findViewById(R.id.scheduler_button);
		final Button button_ument = (Button) findViewById(R.id.ument_button);
		final Button button_message = (Button) findViewById(R.id.message_button);
		final ImageButton button_setting = (ImageButton) findViewById(R.id.setting_button);
		umentsort=(Button) findViewById(R.id.sortument);
		addb.setVisibility(View.VISIBLE);
		umentsort.setVisibility(View.GONE);
		schedulerForXML = View.inflate(this, R.layout.calendar, null);
		schedulerForXML.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		RelativeLayout relative = (RelativeLayout)findViewById(R.id.relativeLayout242);

		final RelativeLayout sundayLayout = (RelativeLayout)findViewById(R.id.relativeLayout4);
		final RelativeLayout mondayLayout = (RelativeLayout)findViewById(R.id.relativeLayout5);
		final RelativeLayout tuesdayLayout = (RelativeLayout)findViewById(R.id.relativeLayout6);
		final RelativeLayout wednesdayLayout = (RelativeLayout)findViewById(R.id.relativeLayout7);
		final RelativeLayout thursdayLayout = (RelativeLayout)findViewById(R.id.relativeLayout8);
		final RelativeLayout fridayLayout = (RelativeLayout)findViewById(R.id.relativeLayout9);
		final RelativeLayout saturdayLayout = (RelativeLayout)findViewById(R.id.relativeLayout10);

		//ScrollView controler
		final ScrollView sv = (ScrollView) findViewById(R.id.scrollView1);

		sundayLayout.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				// Point p = Touch.
				popupView();
				sunday = true;
				return false;
			}
		});

		sundayLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{

					globalx = event.getX();
					globaly = event.getY();

					Log.i("JIM","in mouse down"+ globalx + " " + globaly);
				}

				// JIM
				return false;
				//return true;
			}
		});
		sv.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {

			@Override
			public void onScrollChanged() {
				//
				scrolly = sv.getScrollY();
				Log.i("THIS","MTFK"+sv.getScrollY());
				// TODO Auto-generated method stub

			}
		});

		sv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{

					globalx = event.getX();
					globaly = event.getY();

					Log.i("UPDATE","in mouse down"+ globalx + " " + globaly);
				}
				if(action == MotionEvent.ACTION_UP)
				{
					x2 = event.getX();
					y2 = event.getY();

					//if left to right sweep event on screen
					//Log.i("MOZI", "globalx : "+globalx+"x2 : "+x2);
					if(sv.getHeight()/2 >Math.abs(globaly-y2))
						if(globalx == x2)
						{

						}
					if(globalx <x2)
					{
						if (sv.getWidth()/2 < Math.abs(globalx - x2))
						{
							Log.i("LR","brah");
							prevWeek();

						}
					}
					if(globalx >x2)
					{

						// if right to left sweep event on screen
						if (sv.getWidth()/2 < Math.abs(globalx - x2))
						{
							Log.i("RL","brah");
							nextWeek();
						}
					}
				}

				// JIM
				return false;
				//return true;
			}
		});

		mondayLayout.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				// Point p = Touch.
				popupView();
				monday=true;
				return false;
			}
		});

		mondayLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{

					globalx = event.getX()+mondayLayout.getWidth();
					globaly = event.getY();

					Log.i("JIM","in mouse down"+ globalx + " " + globaly);
				}

				// JIM
				return false;
				//return true;
			}
		});

		tuesdayLayout.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				// Point p = Touch.
				popupView();
				tuesday = true;
				return false;
			}
		});

		tuesdayLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{

					globalx = event.getX()+tuesdayLayout.getWidth()*2;
					globaly = event.getY();

					Log.i("JIM","in mouse down"+ globalx + " " + globaly);
				}
				// JIM
				return false;
				//return true;
			}
		});

		wednesdayLayout.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				// Point p = Touch.
				popupView();
				wednesday=true;
				return false;
			}
		});

		wednesdayLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{

					globalx = event.getX()+wednesdayLayout.getWidth()*3;
					globaly = event.getY();

					Log.i("JIM","in mouse down"+ globalx + " " + globaly);
				}

				// JIM
				return false;
				//return true;
			}
		});

		thursdayLayout.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				// Point p = Touch.
				popupView();
				thursday=true;
				return false;
			}
		});

		thursdayLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{

					globalx = event.getX()+thursdayLayout.getWidth()*4;
					globaly = event.getY();

					Log.i("JIM","in mouse down"+ globalx + " " + globaly);
				}

				// JIM
				return false;
				//return true;
			}
		});

		fridayLayout.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				// Point p = Touch.
				popupView();
				friday=true;
				return false;
			}
		});

		fridayLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{

					globalx = event.getX()+fridayLayout.getWidth()*5;
					globaly = event.getY();

					Log.i("JIM","in mouse down"+ globalx + " " + globaly);
				}


				// JIM
				return false;
				//return true;
			}
		});

		saturdayLayout.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				// Point p = Touch.
				popupView();
				saturday=true;
				return false;
			}
		});

		saturdayLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{

					globalx = event.getX()+saturdayLayout.getWidth()*6;
					globaly = event.getY();

					Log.i("JIM","in mouse down"+ globalx + " " + globaly);
				}

				// JIM
				return false;
				//return true;
			}
		});

		sv.post(new Runnable() {

			@Override
			public void run() {
				int y = sv.getScrollY();
				Log.i("YYYYYYYYYY", ""+y);
				// TODO Auto-generated method stub
				//globaly = globaly-sv.getScrollY();
				sv.scrollTo(0, topSchedule-100);

			}
		});

		// sv.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// int action = event.getAction();
		// if(action == MotionEvent.ACTION_DOWN)
		// {
		//
		// globalx = event.getX();
		// globaly = event.getY();
		//
		// Log.i("RELATIVE","in mouse down"+ globalx + " " + globaly);
		// }
		//
		// // JIM
		// return false;
		// //return true;
		// }
		// });

		// button_scheduler button to the scheduler page
		button_scheduler.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				activated=0;
				addb.setBackgroundResource(R.drawable.title_background);
				addb.setText("+");

				content_layout.removeAllViews();
				content_layout.addView(schedulerForXML);
				jumpScheduler();
				tv.setText("Scheduler");
				addb.setVisibility(View.VISIBLE);
				umentsort.setVisibility(View.GONE);
				button_scheduler.setBackgroundResource(R.drawable.scheduler_clicked);
				button_message.setBackgroundResource(R.drawable.message_unclicked);
				button_setting.setBackgroundResource(R.drawable.setting_unclicked);
				button_ument.setBackgroundResource(R.drawable.ument_unclicked);
			}
		});
		//ument button to the ument page.
		button_ument.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				FileCache ff= new FileCache(context);
				ff.clear();
				// TODO Auto-generated method stub
				activated=1;
				addb.setBackgroundResource(R.drawable.title_background);
				addb.setText("+");
				addb.setVisibility(View.VISIBLE);
				umentsort.setVisibility(View.VISIBLE);
				content_layout.removeAllViews();
				//content_layout.addView(textument);
				umentview = View.inflate(context, R.layout.umentview, null);
				content_layout.addView(umentview);
				PullToRefreshListView lv =(PullToRefreshListView)findViewById(R.id.umentlistview);
				TextView textument=(TextView)findViewById(R.id.textument);

				um= new Ument(context,lv,ms,InClass.this,textument,Uid);

				if(mData.size()==0)
				{
					mData=um.indata();
				}

				um.jumpument(mData);
				tv.setText("Ument");

				//content_layout.removev
				//addb.setVisibility(View.GONE);

				button_scheduler.setBackgroundResource(R.drawable.scheduler_unclicked);
				button_message.setBackgroundResource(R.drawable.message_unclicked);
				button_setting.setBackgroundResource(R.drawable.setting_unclicked);
				button_ument.setBackgroundResource(R.drawable.ument_clicked);

			}
		});

		chattingForXML = View.inflate(this, R.layout.chat_list, null);
		// message button to the message page.
		button_message.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				umentsort.setVisibility(View.GONE);
				activated=2;
				addb.setBackgroundResource(R.drawable.title_background);
				addb.setVisibility(View.INVISIBLE);
				tv.setText("Message");
				//addb.setVisibility(View.GONE);
				content_layout.removeAllViews();
				content_layout.addView(chattingForXML);

				try {
					chat_list = new ChatList(Uid,ms);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!chat_list.hasChat()){
					Toast.makeText(getApplicationContext(),"You do not have history",Toast.LENGTH_SHORT).show();
				}
				else{
					setChatList();
				}

				button_scheduler.setBackgroundResource(R.drawable.scheduler_unclicked);
				button_message.setBackgroundResource(R.drawable.message_clicked);
				button_ument.setBackgroundResource(R.drawable.ument_unclicked);
				button_setting.setBackgroundResource(R.drawable.setting_unclicked);
			}

		});
		settingViewForXML = View.inflate(this, R.layout.setting, null);

		// setting button to the setting page
		button_setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				umentsort.setVisibility(View.GONE);

				// TODO Auto-generated method stub
				activated=3;
				//content_layout.removeAllViewsInLayout();
				tv.setText("Setting");
				content_layout.removeAllViews();
				//setContentView(R.layout.setting);
				content_layout.addView(settingViewForXML);
				//content_layout.addView(tempor);
				// addb.setBackgroundDrawable(R.drawable)
				addb.setBackgroundResource(R.drawable.logout);
				addb.setText("");

				addb.setVisibility(View.VISIBLE);

				button_scheduler.setBackgroundResource(R.drawable.scheduler_unclicked);
				button_message.setBackgroundResource(R.drawable.message_unclicked);
				button_ument.setBackgroundResource(R.drawable.ument_unclicked);
				button_setting.setBackgroundResource(R.drawable.setting_clicked);
				ListView listclass=(ListView) findViewById(R.id.classlist);

				setting_name=(TextView)findViewById(R.id.settingname);
				setting_email=(TextView)findViewById(R.id.settingEmail);

				s= new setting(Uid, ms, context,ts.getClasses(),content_layout,listclass,InClass.this);

				//s.addclasslist(listclass, ts.getClasses(), content_layout);
				im_=(ImageView) findViewById(R.id.setting_image);

				s.load(setting_name,setting_email,radgroup,im_);

				RelativeLayout piclayout = (RelativeLayout) findViewById(R.id.settingpic_);
				myument=(RelativeLayout) findViewById(R.id.setting_myument);

				myument.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Intent intent = new Intent(context, myument.class); intent.putExtra("userId", Uid); intent.putExtra("targetUid", Uid); startActivity(intent); } });
				piclayout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						AlertDialog.Builder builder = new Builder(context);

						builder.setTitle("Choose");

						builder.setPositiveButton("Gallery", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								Intent i = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

								startActivityForResult(i, RESULT_LOAD_IMAGE);
								dialog.dismiss();

								//Main.this.finish();
							}
						});

						builder.setNegativeButton("Camera", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								isExist(Environment.getExternalStorageDirectory()+"/inclass");
								//Calendar c = Calendar.getInstance();
								File file = new File(Environment.getExternalStorageDirectory()+"/inclass",
										Uid+"."+"jpg");
								outPutFile = Uri.fromFile(file);

								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutFile);
								//startActivityForResult(intent, takeCode);

								// TODO Auto-generated method stub
								//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

								//startActivityForResult(intent, 1);

								startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);

								dialog.dismiss();
							}
						});

						builder.create().show();

					}

				});

			}
		});

		//Button updata=(Button)findViewById(R.id.updata);

		// add button listener.
		umentsort.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) {


				PopupMenu popup = new PopupMenu(context, v);
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.popup1, popup.getMenu());

				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						int id = item.getItemId();

						if(id == R.id.time)
						{
							um.changesort("time");

						}
						else if(id == R.id.comment)
						{
							um.changesort("comment");

						}
						else if(id==R.id.like)
						{
							um.changesort("like");
						}

						return true;
					}
				});
				popup.show();



			}

		});

		addb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(activated==0)
				{
					PopupMenu popup = new PopupMenu(context, v);
					MenuInflater inflater = getMenuInflater();
					inflater.inflate(R.menu.popup, popup.getMenu());

					popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							int id = item.getItemId();

							if(id == R.id.add_class)
							{
								activated=4;
								jumpAddclass();

							}
							else if(id == R.id.add_event)
							{

								Intent intent = new Intent(context, Addument_event.class);
								//								String textStartTime = timeConvert(eventStartHour, eventStartMin);
								//								String textEndTime = timeConvert(eventEndHour, eventEndMin);
								//								SimpleDateFormat format1 = new SimpleDateFormat("MMddyyyy");
								//								String format = format1.format(cal2.getTime());
								intent.putExtra("uid",Uid);
								//								intent.putExtra("StartTime",textStartTime);
								//								intent.putExtra("EndTime", textEndTime);
								//								intent.putExtra("format", format);
								//fix8
								startActivityForResult(intent,9);

								//								activated=4;
								//								jumpAddevent();
								//fix


							}

							return true;
						}
					});
					popup.show();

				}
				else if(activated==1)
				{
					//Toast.makeText(getApplicationContext(),"this is ument add",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(InClass.this, addument.class);

					intent.putExtra("un", Uid);

					startActivityForResult(intent, 3);

				}
				else if(activated==3){
					SaveSharedPreference.clearPreference(context);
					try {
						exit_program();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

	}

	protected void setChatList() {
		// TODO Auto-generated method stub

		ListView chat_list_view = (ListView) findViewById(R.id.chatting_list);
		adapter = new ChatList_Adapter(context);


		chat_list_view.setAdapter(adapter);

	}


	public static void isExist(String path) {
		File file = new File(path);

		if (!file.exists()) {
			file.mkdir();
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==3)
		{
			um.updata();
		}

		if(requestCode==4)
		{
			topSchedule = 0;
			jumpScheduler();
		}
		if(requestCode==5)
		{

			ts.getClasses(ms);
			s.updata_classlist(ts.getClasses());
		}

		if(requestCode==6)
		{
			topSchedule = 0;
			jumpScheduler();

		}
		if(requestCode==7)
		{
			Bundle res = data.getExtras();
			String result = res.getString("umentid");
			um.updatacomment(result);
		}
		if(requestCode==8)
		{
			Bundle res = data.getExtras();
			String result = res.getString("umentid");
			um.updatacomment(result);
		}
		if(requestCode==9)
		{
			topSchedule = 0;
			jumpScheduler();
		}


		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaColumns.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			//			im_.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			setImageSrc(im_,picturePath);
			s.setpic(picturePath);




		}

		if (resultCode == RESULT_OK)
		{  
			if(requestCode == REQUEST_CODE_TAKE_PICTURE)
			{


				BitmapFactory.Options opts = new BitmapFactory.Options();


				picturePath=outPutFile.getPath();
				//				Bitmap bitmap = BitmapFactory.decodeFile(outPutFile.getPath(),
				//						opts);
				//
				//				im_.setImageBitmap(bitmap);
				setImageSrc(im_,picturePath);

				s.setpic(picturePath);

			}
		}
	}





	@Override
	public void onBackPressed(){


		if(activated==4)
		{
			topSchedule = 0;
			jumpScheduler();
		}
		else
		{
			close = false;
			setContentView(R.layout.exit_dialog);
			Button yes = (Button) findViewById(R.id.btn_yes);
			Button no = (Button) findViewById(R.id.btn_no);
			yes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//				FileCache f= new FileCache(context);
					//				f.clear();
					try {
						exit_program();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			no.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(ii==2)
					{
						jumpAddclass();
					}
					else if(ii==3)
					{
						topSchedule = 0;
						jumpScheduler();
					}
					else if(ii==1)
					{
						startActivity(i);
						System.exit(0);
					}
				}
			});
		}
	}

	private void exit_program() throws SQLException{

		FileCache f= new FileCache(context);
		f.clear();

		Intent stopIntent = new Intent(this, MsgService.class);  
		stopService(stopIntent);  
		ms.closeConn();

		finish();
	}






	/**
	 * Draw class list
	 * @param schedulerView2
	 */
	private void DrawScheduler() {

		//	content_layout = (LinearLayout) findViewById(R.id.cont_layer);
		schedulerForXML = View.inflate(this, R.layout.calendar, null);

		content_layout.removeAllViews();

		content_layout. addView(schedulerForXML);
		weeklyControl();

		// TODO Auto-generated method stub
		//Recieve class list from DB
		ts.getClasses(ms);
		schedulerView.addList(ts.getClasses());
		ArrayList<ClassInfo> temp = schedulerView.getClassInfos();

		es.getEvents(ms);
		eventView.addList(es.getEvents());
		allEvents = eventView.getEventInfos();
		ArrayList<EventInfo> temp2 = eventView.getEventInfos();

		ArrayList<String> dayList = new ArrayList<String>();

		ArrayList<String> eDayList = new ArrayList<String>();

		//Set variable
		Context c = context;
		RelativeLayout ll = new RelativeLayout(c);
		String days = null;
		String days2 = null;
		boolean online = false;
		String day;
		String startTime;
		String finTime;
		String name;
		String name2;
		String sec;
		String cid2;
		String startDay = null;
		String endDay = null;
		int top, bottom;
		int dayCount=0;
		int count = 0;
		int count2 = 0;

		//Event variable

		String evDay;
		String eStartTime;
		String eFinTime;
		String eStartDay;
		String eEndDay;
		String eName;
		String eid;
		int eTop = 0;
		int eBottom = 0;

		int tempsize2 = temp2.size();

		///////////////////////////////////////////////////////////////


		//Set receive time
		time = cal2.getTimeInMillis();

		Log.i("HERE!!", cal2.getTime().toString());
		dayCount = cal2.get(Calendar.DAY_OF_WEEK);
		//Cal week day
		switch(dayCount)
		{
		case 1:
			count = 0;

			break;
		case 2:
			count = -1;

			break;
		case 3:
			count = -2;

			break;
		case 4:
			count = -3;

			break;
		case 5:
			count = -4;

			break;
		case 6:
			count = -5;

			break;
		case 7:
			count = -6;

			break;
		}
		//Set format
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");

		c2 = cal2;
		c2.add(Calendar.DAY_OF_MONTH, count);


		String format2 = format1.format(c2.getTime());


		c2.add(Calendar.DAY_OF_MONTH, 6);
		String format = format1.format(c2.getTime());

		//Set textview
		date = (TextView)findViewById(R.id.TextView01);
		date.setTextSize(21);
		date.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		date.setText(format2 + " ~ "+format);

		int tempsize = temp.size();

		date.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//LinearLayout viewGroup = (LinearLayout) findViewById(R.id.popup_c);
				LayoutInflater layoutInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = layoutInflater.inflate(R.layout.popup_calendar, null);
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

				//				   DatePicker dp = (DatePicker) findViewById(R.id.datepickerS);

				Button update = (Button) layout.findViewById(R.id.update_cal);
				update.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {

						boolean less10 = false;
						int Month = dp.getMonth()+1;
						int Day = dp.getDayOfMonth();
						if(Day<10)
						{
							less10 = true;
						}
						int Year = dp.getYear();
						String Month2 = String.valueOf(Month);
						if(!Month2.equals("10")&&!Month2.equals("11")&&!Month2.equals("12"))
						{
							Month2 = "0"+Month2;
						}
						String Day2 = String.valueOf(Day);
						if(less10)
						{
							Day2 = "0"+Day2;
						}
						String Year2 = String.valueOf(Year);

						String finals = Month2+"/"+Day2+"/"+Year2;

						//final EditText startdate = (EditText)findViewById(R.id.startDay);
						date.setText(finals);

						cal2.set(Calendar.MONTH, Month-1);
						cal2.set(Calendar.DAY_OF_MONTH, Day);
						Log.i("Calendar", cal2.getTime().toString());
						SimpleDateFormat format1 = new SimpleDateFormat("MMddyyyy");
						String format = format1.format(cal2.getTime());
						Log.i("FORMAT", format);






						////////////////////////////////////////////
						popup.dismiss();

						jumpScheduler();
					}
				});

			}
		});

		//Check class's valid
		for(int classIndex = 0; classIndex < tempsize; classIndex++)
		{

			startDay = temp.get(classIndex).getStartDay();
			endDay = temp.get(classIndex).getEndDay();
			//startDay = "01012015";
			//endDay = "05012015";

			int sMonth = Month(startDay);
			int sDay = Day(startDay);
			int eMonth = Month(endDay);
			int eDay = Day(endDay);

			cal.set(Calendar.MONTH, sMonth-1);
			cal.set(Calendar.DAY_OF_MONTH, sDay);

			long startDate = cal.getTimeInMillis();
			//End day of class
			cal3.set(Calendar.MONTH, eMonth-1);
			cal3.set(Calendar.DAY_OF_MONTH, eDay);

			long endDate = cal3.getTimeInMillis();

			if(time < startDate || time > endDate)
			{
				//temp2.add(temp.get(classIndex));
				temp.remove(classIndex);

				classIndex=classIndex-1;
				tempsize = temp.size();
			}	
		}

		c2.add(Calendar.DAY_OF_MONTH, -6);



		for(int eventIndex = 0; eventIndex < tempsize2; eventIndex++)
		{	
			eStartDay = temp2.get(eventIndex).getStartDay();
			eEndDay = temp2.get(eventIndex).getEndDay();

			int seMonth = Month(eStartDay);			
			int seDay = Day(eStartDay);
			if(eEndDay.equals("")){
				eEndDay = eStartDay;
			}
			int eeMonth = Month(eEndDay);
			int eeDay = Day(eEndDay);
			//

			char mon1 = format2.charAt(0);
			char mon2 = format2.charAt(1);
			char day1 = format2.charAt(3);
			char day2 = format2.charAt(4);
			char year1 = format2.charAt(6);
			char year2 = format2.charAt(7);
			char year3 = format2.charAt(8);
			char year4 = format2.charAt(9);

			String mon = Character.toString(mon1)+Character.toString(mon2);
			String date = Character.toString(day1)+Character.toString(day2);
			String year = Character.toString(year1)+Character.toString(year2)+Character.toString(year3)+Character.toString(year4);

			int intYear = Integer.parseInt(year);
			int intDate = Integer.parseInt(date);
			int intMon = Integer.parseInt(mon);
			int dayCount2 = 0;
			int dayCount3 = 0;

			c3.set(Calendar.YEAR, intYear);
			c3.set(Calendar.MONTH, intMon-1);
			c3.set(Calendar.DAY_OF_MONTH, intDate);

			long weekStartTime = c2.getTimeInMillis();

			cal.set(Calendar.MONTH, seMonth-1);
			cal.set(Calendar.DAY_OF_MONTH, seDay);

			dayCount3 = cal.get(Calendar.DAY_OF_WEEK);
			//Cal week day
			switch(dayCount3)
			{
			case 1:
				count = 0;

				break;
			case 2:
				count = -1;

				break;
			case 3:
				count = -2;

				break;
			case 4:
				count = -3;

				break;
			case 5:
				count = -4;

				break;
			case 6:
				count = -5;

				break;
			case 7:
				count = -6;

				break;
			}

			cal.add(Calendar.DAY_OF_MONTH, count);

			//			cal.set(Calendar.MONTH, seMonth-1);
			//			cal.set(Calendar.DAY_OF_MONTH, seDay);
			//			//
			long eStartDate = cal.getTimeInMillis();


			cal3.set(Calendar.MONTH, eeMonth-1);
			cal3.set(Calendar.DAY_OF_MONTH, eeDay);
			//			

			dayCount2 = cal3.get(Calendar.DAY_OF_WEEK);
			//Cal week day
			switch(dayCount2)
			{
			case 1:
				count = 6;

				break;
			case 2:
				count = 5;

				break;
			case 3:
				count = 4;

				break;
			case 4:
				count = 3;

				break;
			case 5:
				count = 2;

				break;
			case 6:
				count = 1;

				break;
			case 7:
				count = 0;

				break;
			}
			//Set format

			cal3.add(Calendar.DAY_OF_MONTH, count);
			//			c3.add(Calendar.DAY_OF_MONTH, 6);





			String format4 = format1.format(cal3.getTime());
			Log.i("AAA", format4);
			String format3 = format1.format(cal.getTime());

			long zz = cal.getTimeInMillis();

			long eEndDate = cal3.getTimeInMillis();

			String dateString = DateFormat.format("MM/dd/yyyy", new Date(time)).toString();
			Log.i("time", "AAAAAAAAAAAAAA"+dateString + " "+format3);
			//if(time < startDate || time > endDate)

			if( time < eStartDate|| time > eEndDate)
			{
				temp2.remove(eventIndex);
				eventIndex = eventIndex-1;
				tempsize2 = temp2.size();
			}	
		}

		for(int i2 =0; i2<temp2.size(); i2++)
		{
			evDay = temp2.get(i2).getDay();
			eStartTime = temp2.get(i2).getStartTime();
			eFinTime = temp2.get(i2).getEndTime();
			eName = temp2.get(i2).getName();

			eid = temp2.get(i2).getEid();
			final int eid3 = Integer.parseInt(eid);


			//float cid2= Float.parseFloat(cid);
			eTop = time(eStartTime);

			if(topSchedule == 0)
			{
				topSchedule = eTop;
			}
			if(topSchedule > eTop)
			{
				topSchedule = eTop;
			}

			eBottom = time(eFinTime);

			if(evDay.equals("TBA"))
			{
				day = "X";
			}

			for(int i = 0; i<evDay.length(); i++)
			{
				eDayList.add(i, Character.toString(evDay.charAt(i)));
			}
			//Random rand = new Random();
			int r = 211;
			int g = 211;
			int b = 211;
			for(int i = 0; i<evDay.length(); i++)
			{



				if(eDayList.get(i).equals("X"))
				{
					online = true;
				}
				else if(eDayList.get(i).equals("S"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout4);
					online = false;

				}
				else if(eDayList.get(i).equals("M"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout5);
					online = false;

				}
				else if(eDayList.get(i).equals("T"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout6);
					online = false;

				}
				else if(eDayList.get(i).equals("W"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout7);
					online = false;

				}
				else if(eDayList.get(i).equals("H"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout8);
					online = false;

				}
				else if(eDayList.get(i).equals("F"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout9);
					online = false;

				}
				else if(eDayList.get(i).equals("A"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout10);
					online = false;

				}
				//Draw button for each class

				btn2 = new Button(this);
				btn2.setLayoutParams(new LayoutParams(70,eBottom-eTop));

				btn2.setY(eTop);
				btn2.setId(i);
				btn2.setPadding(0, 0, 0, 0);

				btn2.setTextSize(9);

				btn2.setText(eName);

				btn2.setBackgroundColor(Color.rgb(r, g, b));

				btn2.getBackground().setAlpha(64);

				////////////////////////////////////////////////////////////
				btn2.setOnClickListener(new View.OnClickListener()  {



					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, Additem_event.class);
						intent.putExtra("eid", eid3+"");
						intent.putExtra("uid", Uid);
						intent.putExtra("type", "del");

						startActivityForResult(intent, 6); 

					}
				});


				ll.addView(btn2);

			}


		}








		//Check class list and draw button
		for(int i2 =0; i2<temp.size(); i2++)
		{


			day = temp.get(i2).getDay();
			startTime = temp.get(i2).getStartTime();
			finTime = temp.get(i2).getEndTime();
			name = temp.get(i2).getDepartment();
			name2 = temp.get(i2).getClassNumber();
			sec = temp.get(i2).getSection();

			cid2 = temp.get(i2).getCid();
			final int cid3 = Integer.parseInt(cid2);


			//float cid2= Float.parseFloat(cid);
			top = time(startTime);

			if(topSchedule == 0)
			{
				topSchedule = top;
			}
			if(topSchedule > top)
			{
				topSchedule = top;
			}

			bottom = time(finTime);

			if(day.equals("TBA"))
			{
				day = "X";
			}

			for(int i = 0; i<day.length(); i++)
			{
				dayList.add(i, Character.toString(day.charAt(i)));
			}
			Random rand = new Random();
			int r = rand.nextInt();
			int g = rand.nextInt();
			int b = rand.nextInt();
			for(int i = 0; i<day.length(); i++)
			{

				if(dayList.get(i).equals("X"))
				{
					online = true;
				}
				else if(dayList.get(i).equals("S"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout4);
					online = false;

				}
				else if(dayList.get(i).equals("M"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout5);
					online = false;

				}
				else if(dayList.get(i).equals("T"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout6);
					online = false;

				}
				else if(dayList.get(i).equals("W"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout7);
					online = false;

				}
				else if(dayList.get(i).equals("H"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout8);
					online = false;

				}
				else if(dayList.get(i).equals("F"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout9);
					online = false;

				}
				else if(dayList.get(i).equals("A"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout10);
					online = false;

				}
				//Draw button for each class

				btn = new Button(this);
				btn.setLayoutParams(new LayoutParams(300,bottom-top));

				btn.setY(top);
				btn.setId(i);
				btn.setPadding(0, 0, 0, 0);
				btn.setTextSize(9);

				btn.setText(name+"\n"+name2+"\n"+"00"+sec);
				
				////
				
				int color = name.hashCode();
				
				btn.setBackgroundColor(Color.rgb(color%10000, color%100, color) );
				btn.getBackground().setAlpha(180);
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					//set Button listener and call information page
					public void onClick(View v) 
					{

						Intent intent = new Intent(context,ClassInformation.class);
						classInformationNumber = cid3;

						intent.putExtra("cid",classInformationNumber+"" );
						intent.putExtra("uid", Uid);
						startActivityForResult(intent, 4);


					}
				});

				ll.addView(btn);

			}
		}
	}


//	private int setColor(String name) {
//		// TODO Auto-generated method stub
//		int retu = 0;
//		for(int i = 0 ; i < name.length(); i ++){
//			retu += name.charAt(i);
//		}
//		return retu;
//	}

	/**
	 * calculate week by next&prev button
	 * @param schedulerView2
	 */
	public void weeklyControl(){


		Button next = (Button) findViewById(R.id.Cnext);
		Button prev = (Button) findViewById(R.id.Cprev);

		next.setOnTouchListener(new OnTouchListener() {
			long time = cal.getTimeInMillis();
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{
					cal2.add(Calendar.DATE, 7);
					Log.i("Calendar", cal2.getTime().toString());
					SimpleDateFormat format1 = new SimpleDateFormat("MMddyyyy");
					String format = format1.format(cal2.getTime());
					Log.i("FORMAT", format);


					//EditText vw = (EditText)findViewById(R.id.TextView01);
					//vw.setText(cal2.getTime().toString());
					//					DrawEvent();
					topSchedule = 0;
					jumpScheduler();
				}
				return false;
			}
		});

		//when calss prev button -7
		prev.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN)
				{
					cal2.add(Calendar.DATE, -7);
					Log.i("Calendar", cal2.getTime().toString());
					SimpleDateFormat format1 = new SimpleDateFormat("MMddyyyy");
					String format = format1.format(cal2.getTime());
					Log.i("FORMAT", format);
					topSchedule = 0;
					jumpScheduler();
				}
				return false;
			}
		});
	}

	public void nextWeek()
	{
		cal2.add(Calendar.DATE, 7);

		topSchedule = 0;
		jumpScheduler();
	}

	public void prevWeek()
	{
		cal2.add(Calendar.DATE, -7);

		topSchedule = 0;
		jumpScheduler();
	}

	private int mod(int x, int y)
	{
		int result = x % y;
		return result < 0? result + y : result;
	}

	public void popupView()
	{
		// TODO Auto-generated method stub
		int time = 840+(int)globaly;
		int stime = time - 60;
		int etime = time + 60;
		eventStartHour = stime/120;
		eventStartMin = (mod(stime, 120)/2);
		eventEndHour = etime/120;
		eventEndMin = (mod(etime, 120)/2);



		RelativeLayout dayLayout = new RelativeLayout(context);

		View addEventViewForXML = View.inflate(context, R.layout.addevent, null);


		if(sunday)
		{
			dayLayout = (RelativeLayout) findViewById(R.id.relativeLayout4);

		}
		else if(monday)
		{
			dayLayout = (RelativeLayout) findViewById(R.id.relativeLayout5);
		}
		else if(tuesday)
		{
			dayLayout = (RelativeLayout) findViewById(R.id.relativeLayout6);
		}
		else if(wednesday)
		{
			dayLayout = (RelativeLayout) findViewById(R.id.relativeLayout7);
		}
		else if(thursday)
		{
			dayLayout = (RelativeLayout) findViewById(R.id.relativeLayout8);
		}

		else if(friday)
		{
			dayLayout = (RelativeLayout) findViewById(R.id.relativeLayout9);
		}
		else if(saturday)
		{
			dayLayout = (RelativeLayout) findViewById(R.id.relativeLayout10);
		}

		//View menuItemView = findViewById(R.layout.calendar);

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_event, null);

		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);

		popup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		popup.setFocusable(true);
		popup.setBackgroundDrawable(new ColorDrawable(
				android.graphics.Color.TRANSPARENT));

		popup.showAtLocation(layout, Gravity.NO_GRAVITY, (int)globalx, (int)globaly-scrolly+200);

		Button update = (Button) layout.findViewById(R.id.update_event);
		update.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(context, Addument_event.class);
				String textStartTime = timeConvert(eventStartHour, eventStartMin);

				if(sunday==true)
				{
					intent.putExtra("D", "sunday");
					sunday=false;
				}
				else if(monday==true)
				{
					intent.putExtra("D", "monday");
					cal2.add(Calendar.DAY_OF_MONTH, 1);
					monday=false;
				}
				else if(tuesday==true)
				{
					intent.putExtra("D", "tuesday");
					cal2.add(Calendar.DAY_OF_MONTH, 2);
					tuesday=false;
				}
				else if(wednesday==true)
				{	
					intent.putExtra("D", "wednesday");
					cal2.add(Calendar.DAY_OF_MONTH, 3);
					wednesday=false;

				}
				else if(thursday==true)
				{	
					intent.putExtra("D", "thursday");
					cal2.add(Calendar.DAY_OF_MONTH, 4);
					thursday=false;


				}
				else if(friday==true)
				{
					intent.putExtra("D", "friday");
					cal2.add(Calendar.DAY_OF_MONTH, 5);
					friday=false;
				}
				else if(saturday==true)
				{
					intent.putExtra("D", "saturday");
					cal2.add(Calendar.DAY_OF_MONTH, 6);
					saturday=false;
				}

				String textEndTime = timeConvert(eventEndHour, eventEndMin);
				SimpleDateFormat format1 = new SimpleDateFormat("MMddyyyy");
				String format = format1.format(cal2.getTime());
				intent.putExtra("uid",Uid);
				intent.putExtra("StartTime",textStartTime);
				intent.putExtra("EndTime", textEndTime);
				intent.putExtra("format", format);
				//fix8
				startActivityForResult(intent,9);

				popup.dismiss();
			}
		});

	}



	/**
	 * Find the day
	 * @param date
	 * @return
	 */
	public int Day(String date)
	{
		//03231987

		char day1 = date.charAt(2);
		char day2 = date.charAt(3);


		String day11 = Character.toString(day1);
		String day22 = Character.toString(day2);


		int day111 = Integer.parseInt(day11);
		int day222 = Integer.parseInt(day22);

		return  (day111*10)+day222;

	}
	/**
	 * Find the Month
	 * @param date
	 * @return
	 */
	public int Month(String date)
	{
		char month1 = date.charAt(0);
		char month2 = date.charAt(1);

		String month11 = Character.toString(month1);
		String month22 = Character.toString(month2);

		int month111 = Integer.parseInt(month11);
		int month222 = Integer.parseInt(month22);

		return (month111*10)+month222;
	}
	private int time(String time)
	{

		char hour = ' ';
		char hour2 = ' ';
		char min = ' ';
		char min2 = ' ';
		char ampm = ' ';
		if(time.equals("TBA"))
		{
			return 0;
		}
		hour = time.charAt(0);
		hour2 = time.charAt(1);
		min = time.charAt(3);
		min2 = time.charAt(4);
		ampm = time.charAt(5);

		String s = Character.toString(hour);
		String s1 = Character.toString(hour2);
		String s2 = Character.toString(min);
		String s3 = Character.toString(min2);
		String s4 = Character.toString(ampm);

		int x = Integer.parseInt(s);
		int x1 = Integer.parseInt(s1);
		int x2 = Integer.parseInt(s2);
		int x3 = Integer.parseInt(s3);


		int time1 = x*1200;
		int time2 = x1*120;
		int time3 = x2*(120/6);
		int time4 = x3*(12/6);
		int finalV = 0;
		if(x == 1 && x1 == 2 && s4.equals("P"))
		{
			s4 = "A";
		}
		if(s4.equals("A"))
		{
			finalV = (time1+time2+time3+time4)-840;
		}
		else
		{
			finalV = (time1+time2+time3+time4)+600;
		}

		return finalV;
	}






	public boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}




	public class ChatList_Adapter extends BaseAdapter
	{

		private LayoutInflater mInflater;
		public ChatList_Adapter(Context context) {  
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return chat_list.getPeople().size();
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
			final String userId = chat_list.getPeople().get(position);
			final Context newContext = context;
			convertView = new View(newContext);
			convertView = mInflater.inflate(R.layout.individual_chat_room, null);
			Button deleteButton = (Button) convertView.findViewById(R.id.individual_erase);
			final TextView text = (TextView)convertView.findViewById(R.id.individual_user_context);
			ImageView pic=(ImageView)convertView.findViewById(R.id.individual_user_photo);
			final String name=chat_list.getinfo().get(position).get("name").toString();
			text.setText(chat_list.getinfo().get(position).get("name").toString());


			//final change
			final String toUid = chat_list.getPeople().get(position);
			if(newMessages.contains(toUid)){
				text.setTextColor(Color.RED);
			}


			ImageLoader imageLoader=new ImageLoader(newContext); 

			if(chat_list.getinfo().get(position).get("pic")!=null)
			{
				imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+chat_list.getinfo().get(position).get("pic").toString(), pic);
			}



			pic.setOnClickListener (new View.OnClickListener() {

				public void onClick(View v) {

					Intent intent = new Intent(newContext, UserInformationActivity.class);
					//Log.e("test", chat_list.getinfo().get(position).get("uid").toString());

					intent.putExtra("targetUid", userId);
					intent.putExtra("userId", Uid);
					newContext.startActivity(intent);

				}
			});

			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, ChatClient.class);
					intent.putExtra("un", Uid);
					intent.putExtra("toWhom", userId);
					intent.putExtra("name", name);

					es.getEvents(ms);
					allEvents = es.getEvents();
					intent.putParcelableArrayListExtra("allEvents", allEvents);


					// final changes
					newMessages.remove(toUid);
					text.setTextColor(Color.rgb(144, 144, 144));

					newContext.startActivity(intent);
				}
			});

			deleteButton.setBackgroundResource(R.drawable.individual_chat_erase_selector);
			deleteButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {


					// TODO Auto-generated method stub

					//Log.i("chatsize", chats_map.size()+"");
					chat_list.delete(userId);
					Chathistory ch= new Chathistory(Uid);
					ch.delete(userId);
					for (int i = 0; i<chat_list.getPeople().size(); i ++){
						Log.i("TEST" , "TEST CHATS. previous" + chat_list.getPeople().get(i));
					}
					String hello = userId;
					Log.i("posittoon",position+"");

					chat_list.getPeople().remove(position);
					for (int i = 0; i<chat_list.getPeople().size(); i++){
						Log.i("TEST" , "TEST CHATS. after" + chat_list.getPeople().get(i));
					}

					try {
						chat_list = new ChatList(Uid,ms);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					setChatList();
				}
			});
			return convertView;

		}

	}

	private void setImageSrc(ImageView imageView, String imagePath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = getImageScale(imagePath);
		Bitmap bm = BitmapFactory.decodeFile(imagePath, option);
		imageView.setImageBitmap(bm);
	}

	private static int   IMAGE_MAX_WIDTH  = 480;
	private static int   IMAGE_MAX_HEIGHT = 960;

	/**
	 * scale image to fixed height and weight
	 * 
	 * @param imagePath
	 * @return
	 */
	private static int getImageScale(String imagePath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		// set inJustDecodeBounds to true, allowing the caller to query the bitmap info without having to allocate the
		// memory for its pixels.
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, option);

		int scale = 1;
		while (option.outWidth / scale >= IMAGE_MAX_WIDTH || option.outHeight / scale >= IMAGE_MAX_HEIGHT) {
			scale *= 2;
		}
		return scale;
	}




}
