/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * service for recieve the message or send message.
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


/*
 * new class extends the service.  
 */
public class MsgService extends Service {
	
	final String ip = "155.98.109.164";
	final int port = 7000;

	// buffer reader and writer
	private BufferedReader inMsg;
	private BufferedWriter outMsg;
	private String Uid;
	
	private boolean status;
	Socket socket;
	
	String r_msg;
	
	private boolean lock = false;
	
	/*
	 * create the new service
	 * (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override  
    public void onCreate() {  
        super.onCreate();  
        
        Log.d("TAG", "onCreate() executed");  
    }  
	/*
	 * start the service. 
	 * (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        Log.d("TAG", "onStartCommand() executed");  
        
        Uid = intent.getStringExtra("Uid");
        connectServer();
        return super.onStartCommand(intent, flags, startId);  
    }  
     /*
      * destory the service. 
      * (non-Javadoc)
      * @see android.app.Service#onDestroy()
      */
    @Override  
    public void onDestroy() { 
    	try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        super.onDestroy();  
    }  
    /*
     * bind the service. 
     * (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MyBinder();
	}
	/*
	 * helper for binder. 
	 */
	public class MyBinder extends Binder
	{
		public String SendMessage( String toWhom_id,String s)throws IOException
		{
			outMsg.write("MESSAGE: "+toWhom_id + " "+ s + "\r\n");
			outMsg.flush();
//			ReceiveMsg();
			return r_msg;
		}
	}

	/*
	 * connect the server. 
	 */
	public void connectServer() {
		new Thread() {
			public void run() {
				try {
					socket = new Socket(ip,port);
					//Toast.makeText(context, "Status : Online", Toast.LENGTH_SHORT).show();
					//Log.d("[Client]"," Server connected !!");

					inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					outMsg = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

					// when the user logs in.
					outMsg.write("USER: "+Uid+"\r\n");
					outMsg.flush();
					ReceiveMsg();

				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	/*
	 * recive the message. 
	 */
	public void ReceiveMsg()
	{
		String msg;
		status=true;
		while(status) {
			try{
				if (lock == false)
				{
					msg = inMsg.readLine();

					//m = gson.fromJson(msg, Message.class);
					r_msg = msg;

					
					lock = true;
					mHandler.sendEmptyMessage(0);
				}
				
			}
			catch(IOException e) {
				e.printStackTrace();
				status = false;
			}
		}	
		
	}
	/*
	 * new handler for recive the message. 
	 */
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			
			 Log.d("Message", r_msg);
			 
			 Intent intent = new Intent("com.example.inclass.RECEIVER"); 
			 
             intent.putExtra("msg",r_msg );  
             sendBroadcast(intent); 
			
			//Toast.makeText(getApplicationContext(),r_msg,Toast.LENGTH_SHORT).show();

			lock = false;
		}
	};

}
