/**
 * Team C&K
 * @author yushao Seongman Kim 
 * 
 * Chatting Client Side.
 * Actual UI
 */

package com.example.inclass;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.text.Html;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemLongClickListener;

public class ChatClient extends Activity {
	//View loginView, logoutView;



	public EditText chat_id,chat_txt;
	public ListView chatList;

	private MyServiceConnection conn = null;  

	// Receives history of chatting
	ArrayList<String> chats;
	// helps dynamical change
	ChatAdapter chatAdapter;
	Button back;


	ImageButton send_event;


	// information of chatting.
	String login_id;
	String toWhom_id;
	private Context context;
	String r_msg;
	String name;

	ArrayList<EventInfo> allEvents;
	boolean isEvent = false;

	mysql ms ;





	// mesage counter
	int mcnt = 0;
	int i = 0;

	private MsgReceiver msgReceiver; 

	private MsgService.MyBinder myBinder;

	TakingE_studentSide es;
	Chathistory chatHistory;


	//for event selector
	private int eventSelected = 0;
	private boolean connected;

	String eventString;





	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		eventString = "";

		chatAdapter = new ChatAdapter(context);
		chats = new ArrayList<String>();
		allEvents = new ArrayList<EventInfo>();
		ms = new mysql();


		connected = false;

		new Thread() { 
			@Override
			public void run() {
				ms.connectDB();

			} 
		}.start();


		setContentView(R.layout.message);
		Intent intent=getIntent();
		// grabs sender id and receiver id from another activity
		login_id=intent.getStringExtra("un");
		toWhom_id=intent.getStringExtra("toWhom");
		name=intent.getStringExtra("name");
		//allEvents = intent.getParcelableArrayListExtra("allEvents");

		es = new TakingE_studentSide(login_id, name);
		new Thread() { 
			@Override
			public void run() {
				ms.connectDB();
				es.getEvents(ms);
				connected = true;

			} 
		}.start();


		while(!connected){

		}
		allEvents = es.getEvents();

		TextView tv=(TextView)findViewById(R.id.who_talking_with);
		tv.setText(name);

		chatHistory= new Chathistory(login_id);
		send_event = (ImageButton) findViewById(R.id.send_event);
		//String[] get = ch.readTxtFile1(toWhom_id).split("\n");

		chats=chatHistory.readTxtFile(toWhom_id);


		//chatting text
		chat_txt = (EditText)findViewById(R.id.chat_txt);
		back = (Button)findViewById(R.id.chatting_back_button);

		chatList =(ListView) findViewById(R.id.chat_list);
		chatList.setAdapter(chatAdapter);

		msgReceiver = new MsgReceiver();  
		IntentFilter intentFilter = new IntentFilter();  
		intentFilter.addAction("com.example.inclass.RECEIVER");  
		registerReceiver(msgReceiver, intentFilter);

		Intent service = new Intent(this,
				MsgService.class);
		conn = new MyServiceConnection();

		this.bindService(service, conn, BIND_AUTO_CREATE); 

		Toast.makeText(this, login_id+" send message to "+toWhom_id, Toast.LENGTH_SHORT).show();
		chatList.setOnItemLongClickListener( new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					View view, final int position, long id) {
				// TODO Auto-generated method stub
				//Log.e("message", chats.get(position));

				AlertDialog.Builder builder = new AlertDialog.Builder(context);  

				builder.setTitle("Delete Message");  
				builder.setMessage("Do you want to delete this message?");  
				builder.setPositiveButton("Yes",  
						new DialogInterface.OnClickListener() {  
					public void onClick(DialogInterface dialog, int whichButton) {  
						chats.remove(position);
						chatAdapter.notifyDataSetChanged();

						chatHistory.removeonemessage(chats,toWhom_id);
					}  
				});  
				builder.setNeutralButton("No",  
						new DialogInterface.OnClickListener() {  
					public void onClick(DialogInterface dialog, int whichButton) {  
					}  
				});  

				builder.show();  
				return true;
			}

		});
		// event open box
		//for deletion


		//TEST GOES HERE!!!!!
		//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//		builder.setTitle(allEvents.size()+" hi");
		//
		//		ListView modeList = new ListView(this);
		//		String[] stringArray = ArrayListToArray_nameString(allEvents);
		//		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
		//		modeList.setAdapter(modeAdapter);
		//
		//		builder.setView(modeList);
		//		final Dialog dialog = builder.create();




		final AlertDialog.Builder eventOpen = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

		eventOpen.setTitle(Html.fromHtml("<font color='#FF4444'>Select Your Event</font>"));
		//		String[] items = new String[2];
		//		items[0] = allEvents.size()+"";
		//		items[1] = "item2";
		//		ArrayListToArray_nameString(allEvents)
		eventOpen.setSingleChoiceItems(ArrayListToArray_nameString(allEvents), 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Toast.makeText(this, allEvents.get(which).getName(), Toast.LENGTH_SHORT).show();
				Log.i("TdEST", "which : " + which);
				eventSelected = which;

			}
		});
		eventOpen.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Do nothing but close the dialog
				try {
					sendEvent(eventSelected);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				r_msg = "<EVENT> "+eventString;
				dialog.dismiss();
			}


		});

		eventOpen.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Do nothing but close the dialog

				dialog.dismiss();
			}

		});

		send_event.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {

				eventOpen.show();
				//dialog.show();
			}

		});

		back.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				unbindService(conn);
				try {
					ms.closeConn();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
			}

		});


	}

	private void backpressed() throws SQLException {
		// TODO Auto-generated method stub
		ms.closeConn();
		finish();
	}





	private String[] ArrayListToArray_nameString(ArrayList<EventInfo> events) {
		// TODO Auto-generated method stub
		int eventSize = events.size();
		String[] ret = new String[eventSize];
		String form = "";
		for (int i = 0; i < eventSize; i++ ){
			EventInfo temp = events.get(i);
			if(events.get(i).getEndDay() !="")
				form = temp.getName() + "  (" + temp.getDay()+")";
			else
				form = temp.getName() + "  (" + temp.getDay()+")"; 
			ret[i] = form;
		}
		return ret;
	}






	/*
	 * sends message to server with protocol
	 * MESSAGE: userId Hello
	 */
	public void mSendClick(View view) throws IOException {

		r_msg = chat_txt.getText().toString();
		if(!r_msg.equals("")){
			r_msg= r_msg.replaceAll("\n", " ");

			myBinder.SendMessage(toWhom_id, r_msg);
		}
	}

	public void getMessage(String message) {
		//System.out.println("##"+r_msg);
		if(message.equals("ERROR")){
			Log.i("Message","MESSAGE : NOT SENT");
			chats.add("0"+"Error do not send: "+message);
		}
		else if (message.equals("LOGGED_IN")){
			//chats.add(message);
			//Log.i("Logged in","MESSGE : LOG IN");
		}

		else if (message.equals("OK")){
			Log.i("Message","MESSAGE : " + r_msg);
			if(checkIfEvent(r_msg)){
				chats.add("0"+"<"+r_msg.substring(7) +">, This event has been sent");
				chatHistory.writemessgae(toWhom_id, "<"+r_msg.substring(7) +">, This event has been sent","1");
			}
			else{
				chats.add("0"+r_msg);
				chatHistory.writemessgae(toWhom_id, r_msg,"1");
			}
			chatAdapter.notifyDataSetChanged();
			chat_txt.setText("");
		}
		else if (message.equals("ERROR_CLOSED")){
			Log.i("Message", "MESSAGE : ERROR");
		}
		else{
			message = retreivingMsg(message);
			if(checkIfEvent(message)){
				chats.add("1"+ message);
			}
			else{
				chats.add("1"+message);
			}
			chatAdapter.notifyDataSetChanged();
		}

	}
	private Boolean checkIfEvent(String msg){
		String _msg = "";
		String[] temp = msg.split(" ", 2);
		if(temp[0].equals("<EVENT>")){
			return true;
		}
		return false;
	}
	private String retreivingMsg(String msg){
		String _msg = "";
		String[] temp = msg.split(" ", 3);
		toWhom_id = temp[1];
		_msg = temp[2];
		return _msg;
	}

	private boolean ismessage(String msg){
		if(msg.equals("OK") )
		{
			return true;

		}

		String[] temp = msg.split(" ", 3);
		String id=temp[1];
		String id2=toWhom_id;

		if(id.equals(id2))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public class ChatAdapter extends BaseAdapter
	{

		private LayoutInflater mInflater;
		public ChatAdapter(Context context) {  
			this.mInflater = LayoutInflater.from(context);  
		}  
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return chats.size();
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

			String returnMessage = chats.get(position);
			if(returnMessage.charAt(0)=='0'){ // Me sending

				convertView = mInflater.inflate(R.layout.send_message_box, null);
				TextView text = (TextView)convertView.findViewById(R.id.right_user_chat);
				text.setText(returnMessage.substring(1));

			}
			else{
				convertView = mInflater.inflate(R.layout.receive_message_box, null);
				TextView text = (TextView)convertView.findViewById(R.id.left_user_chat);
				String showingMessage = returnMessage.substring(1);
				if(showingMessage.length()>6){
					if(showingMessage.substring(0, 6).equals("<EVENT")){
						text.setTextColor(Color.BLUE);
						String eventName = getEventName(showingMessage);
						final String eventNumber = getEventNumber(showingMessage);
						text.setText("Click to see detail\n\""+eventName+"\"");
						Log.i("all events", "filter: showingmessage" +showingMessage);
						text.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Log.i("Text", "filter click : " + eventNumber );
								Intent intent = new Intent(context, Additem_event.class);
								intent.putExtra("eid", eventNumber+"");
								intent.putExtra("uid", login_id);
								intent.putExtra("type", "add");

								startActivityForResult(intent, 6);
							}
						});
						
					}
					else
						text.setText(showingMessage);
				}
				else
					text.setText(showingMessage);
			}
			return convertView;  
		}
		private String getEventNumber(String showingMessage) {
			// TODO Auto-generated method stub
			int i =0;
			for(; i < showingMessage.length(); i ++){
				if(showingMessage.charAt(i) == '>'){
					break;
				}
			}
			return showingMessage.substring(6, i);
		}
		private String getEventName(String showingMessage) {
			// TODO Auto-generated method stub
			int i =0;
			for(; i < showingMessage.length(); i ++){
				if(showingMessage.charAt(i) == '>'){
					break;
				}
			}
			
			return showingMessage.substring(i+1);
		}

	}

	/*
	 * connection to the service. 
	 */
	public class MyServiceConnection implements ServiceConnection
	{

		public void onServiceConnected(ComponentName name, IBinder service)
		{
			myBinder = (MsgService.MyBinder) service;
		}
		public void onServiceDisconnected(ComponentName name)
		{
		}
	}


	/*
	 * BroadcastReceiver for reveive the message from service 
	 */
	public class MsgReceiver extends BroadcastReceiver{  

		@Override  
		public void onReceive(Context context, Intent intent) {
			String msg = intent.getStringExtra("msg"); 

			if(ismessage (msg)==true)
			{
				getMessage(msg);
			}



		}  

	}  

	private void sendEvent(int which) throws IOException {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "this events eid is :" + allEvents.get(which).getName(), Toast.LENGTH_SHORT).show();
		eventString = allEvents.get(which).getName();
		
		myBinder.SendMessage(toWhom_id, "<EVENT"+allEvents.get(which).getEid()+">"+eventString);

		//		chats.add("0"+eventString);
		//		chatHistory.writemessgae(toWhom_id, eventString,"1");
		//adapter.notifyDataSetChanged();
		isEvent = true;
		chatAdapter.notifyDataSetChanged();

		//chats.add("0"+eid);
		//TEST DELETE ME
		Toast.makeText(this, "this events eid is :" + eventString, Toast.LENGTH_SHORT).show();
	}


}