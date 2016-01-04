/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * @author yushao 
 * CS4500 Senior Project
 * 
 * Class Information 
 */
package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.devsmart.android.ui.HorizontalListView;
import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author yushao
 * build the new activity for the classinformation page
 *
 */
public class ClassInformation extends Activity {
	String uid;
	String cid;
	TextView classname;
	TextView classnumber;
	TextView professor;
	TextView location;
	TextView time;
	TextView studentnumber;
	Button classmates;
	TextView morecomment;
	EditText classcomment;
	//TextView lvcomment;
	Button addcomment;
	Button button_deleteClass;
	Button back;
	//ListView commentList;
	ArrayList<Tuple> commentArray;

	ListView commentListView;
	CommenetAdapter commentAdapter;


	ClassInformationPage classInformationPage;
	Context context;
	HorizontalListView studentImageListForXML;
	StudentImageAdapter_forStudentList_taking studentsImageAdapter;
	mysql ms;

	// for deletion
	AlertDialog alert;
	/*
	 * new create for Ui and set inforamtion
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.classinfor);
		getWindow().setSoftInputMode(  WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		context= this;
		Intent intent=getIntent(); 
		uid=intent.getStringExtra("uid");
		cid=intent.getStringExtra("cid");
		setupViews();
		ms= new mysql();
		back=(Button)findViewById(R.id.classinfo_back);
		commentArray = new ArrayList<Tuple>();






		studentsImageAdapter = new StudentImageAdapter_forStudentList_taking(context,uid);


		commentAdapter = new  CommenetAdapter(context);

		new getinfromation().execute("");

		//set the back listener. 
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
		//set more classmates listener. 

		classmates.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, classmates.class);

				intent.putExtra("uid", uid);
				intent.putExtra("cid", classInformationPage.returnclassinfo().getCid());


				startActivity(intent);


			}

		});
		//set more comment button Listener. 
		morecomment.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, moreclasscomment.class);



				intent.putExtra("cid",classInformationPage.returnclassinfo().getCid());
				intent.putExtra("uid", uid);

				startActivity(intent);

			}

		});

		//for deletion
		AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

		builder.setTitle(Html.fromHtml("<font color='#FF4444'>Confirm</font>"));
		builder.setMessage("Do you want to delete this course from your class list?");

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Do nothing but close the dialog
				new MyTaskdelet().execute("");
				dialog.dismiss();
			}

		});

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing
				dialog.dismiss();
			}
		});
		alert = builder.create();



		// set the delete class button Listener. 
		button_deleteClass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.show();
				int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
				View titleDivider = alert.findViewById(titleDividerId);
				if (titleDivider != null)
					titleDivider.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
			}});
		// add new comment button listener.
		addcomment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(
						Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(morecomment.getWindowToken(), 0);
				if(classcomment.getText().toString().equals(""))
				{
					new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)  
					.setTitle("Caution")  
					.setMessage("Please do not send empty comment!")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						@Override
						public void onClick(DialogInterface dialoginterface, int i){   

						}   
					}).show();   
				}
				else
				{
					Thread commentThread=new Thread(new sendcomment());


					commentThread.start();


					try {
						commentThread.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(getApplicationContext(),"you add comment '"+classcomment.getText()+"' for this class",Toast.LENGTH_SHORT).show();
					classcomment.setText("");

					new getinfromation().execute("");
				}

			}
		});
		//		
	}
	/*
	 * set views from xml 
	 */
	public void setupViews()
	{
		classname = (TextView)findViewById(R.id.classname);
		classnumber=(TextView)findViewById(R.id.classid);
		professor=(TextView)findViewById(R.id.pro);
		location=(TextView)findViewById(R.id.location);
		time=(TextView)findViewById(R.id.time);
		studentnumber = (TextView) findViewById(R.id.studentnumber);
		studentImageListForXML = (HorizontalListView) findViewById(R.id.classmates_list);
		commentListView = (ListView) findViewById(R.id.commentlistview);
		classmates=(Button)findViewById(R.id.classmates);

		classcomment =(EditText)findViewById(R.id.commenttext);
		//lvcomment= (TextView)findViewById(R.id.commentlistview);
		morecomment=(TextView)findViewById(R.id.morecomment);
		addcomment=(Button)findViewById(R.id.commentbutton);
		button_deleteClass = (Button)findViewById(R.id.delete);

		classcomment.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					InputMethodManager imm = (InputMethodManager)getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(morecomment.getWindowToken(), 0);
					if(classcomment.getText().toString().equals(""))
					{
						new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)  
						.setTitle("Caution")  
						.setMessage("Please do not send empty comment!")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							@Override
							public void onClick(DialogInterface dialoginterface, int i){   

							}   
						}).show();   
					}
					else
					{
						Thread commentThread=new Thread(new sendcomment());


						commentThread.start();


						try {
							commentThread.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast.makeText(getApplicationContext(),"you add comment '"+classcomment.getText()+"' for this class",Toast.LENGTH_SHORT).show();
						classcomment.setText("");

						new getinfromation().execute("");
					}
					return true;
				}
				return false;
			}
		});

	}
	/*
	 * new asyncTask for get class information from sql. 
	 */

	private class getinfromation extends AsyncTask<String, Integer, String> {

		//List<String> listcomment = new ArrayList<String>();
		ProgressDialog pd = new ProgressDialog(context);

		protected void onPreExecute() {  
			pd.show();

		}  

		@Override
		protected String doInBackground(String... params) {
			ms.connectDB();

			try {
				classInformationPage= new ClassInformationPage(context, ms, ""+cid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			String sql="select u.uid, u.username, c.* from senior_project.Course_comment as c left join senior_project.User as u on c.uid = u.uid where cid='"+cid+"'order by time desc limit 5;";
			ResultSet rs=null;
			commentArray = new ArrayList<Tuple>();
			try {
				rs=ms.execQuery(sql);
				while(rs.next() )
				{


					String username=rs.getString("username");
					String content=rs.getString("content");
					String comid = rs.getString("comid");
					commentArray.add(new Tuple(username,content,comid));
					//listcomment.add(username+": "+content);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		}


		@Override  
		protected void onPostExecute(String result) { 
			pd.dismiss();

			String d="";
			//			for(String s:listcomment)
			//			{
			//				d=d+s+"\n";
			//			}

			//lvcomment.setText(d);

			studentsImageAdapter.setDataSet(classInformationPage.getStudentsImage());
			studentImageListForXML.setAdapter(studentsImageAdapter);;
			studentsImageAdapter.notifyDataSetChanged();

			commentListView.setAdapter(commentAdapter);
			commentAdapter.notifyDataSetChanged();




			classname.setText(classInformationPage.returnclassinfo().getName());

			classnumber.setText(classInformationPage.returnclassinfo().getClassNumber_()+"-"+classInformationPage.returnclassinfo().getSection());

			professor.setText(classInformationPage.returnclassinfo().getProfessor());

			location.setText(classInformationPage.returnclassinfo().getLocation());

			time.setText(classInformationPage.returnclassinfo().getDay()+" "+classInformationPage.returnclassinfo().getStartTime()+"-"+classInformationPage.returnclassinfo().getEndTime());

			studentnumber.setText("("+classInformationPage.returnclassinfo().getStudentNumber()+")");
			if(!classInformationPage.getstudentids().contains(uid))
			{
				addcomment.setVisibility(View.INVISIBLE);
				button_deleteClass.setVisibility(View.INVISIBLE);
				classcomment.setVisibility(View.INVISIBLE);

			}


		}

	}




	// send the new comment thread. 
	public class sendcomment implements Runnable
	{
		@Override
		public void run()
		{
			try {
				//mData= new ArrayList<HashMap<String, Object>>() ;

				//ResultSet rs=null;

				//INSERT INTO `senior_project`.`Course_comment` (`cid`, `uid`, `content`) VALUES ('1', '1', '1');

				String sql="insert into senior_project.Course_comment(cid, uid, content) VALUES ('"+cid+"','"+uid+"','"+classcomment.getText()+"')";


				int rows=ms.execUpdate(sql);
				//rs=ms.execQuery(sql);
				//Message msg = Message.obtain();

				//msg.what =1;
				//handler.sendMessage(msg);
			}
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/*
	 * delete the class from sql. 
	 */

	private class MyTaskdelet extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) 
		{
			try 
			{

				String sql="DELETE FROM senior_project.Taking where uid='" + uid + "' and cid='"+classInformationPage.returnclassinfo().getCid()+"'";
				ms.execUpdate(sql);
				return "good";

			} catch (SQLException e) 
			{
				e.printStackTrace();
				return null;
			}

			//return null;
		}


		@Override  
		protected void onPostExecute(String result) 
		{  

			Intent intent = new Intent(ClassInformation.this,InClass.class);

			setResult(RESULT_OK, intent);  

			finish();


		}




	}





	public class CommenetAdapter extends BaseAdapter
	{

		private LayoutInflater mInflater;
		public CommenetAdapter(Context context) {  
			this.mInflater = LayoutInflater.from(context);  
		}  
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commentArray.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String username = commentArray.get(position).username;
			String comment = commentArray.get(position).comment;
			String comid = commentArray.get(position).comid;

			convertView = mInflater.inflate(R.layout.classcomment, null);
			TextView usernameTextView = (TextView)convertView.findViewById(R.id.comment_username);
			TextView commentTextView = (TextView)convertView.findViewById(R.id.comment_context);
			usernameTextView.setText(username);
			commentTextView.setText(comment);
			return convertView;  
		}

	}





}
