/**
 * @author yushao, Seongman Kim
 * save and get the chat history. 
 */

package com.example.inclass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
/*
 * 
 * build the constructor
 *
 */
public class Chathistory {
	static String uid="";



/*
 * set the uid
 */
	public Chathistory(String Uid)
	{
		uid=Uid;


	}
/*
 * check the file is exits. 
 */
	public boolean fileIsExists(String s){
		try{
			File f=new File(s);
			if(!f.exists()){
				return false;
			}

		}catch (Exception e)
		{
			// TODO: handle exception
			return false;
		}
		return true;
	}



/*
 * write the message(chat history.)
 * send message is start 0
 * receive message is start 1.
 */
	public void writemessgae(String toid ,String message,String type)
	{
		if(type.equals("1"))
		{
			message="0"+message;
		}
		else 
		{
			message="1"+message;
		}


		String path="/sdcard/ChatHistory/"+uid;
		File file = new File(path);  
		if(file.exists()==false)
		{
			file.mkdirs();  
		}
		String p = path+File.separator+toid+".txt";
		if(fileIsExists(p)==true)
		{

			try {
				writeTxtFile(message,toid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//rewrite(p,message);
		}
		else
		{
			newwrite(p,message);
		}
	}

/*
 * write the message to the new file
 */
	public Boolean  newwrite( String path ,String message){  


		FileOutputStream outputStream = null;  
		try {  
		 
			outputStream = new FileOutputStream(new File(path));  
			String msg = new String(message);  
			outputStream.write(msg.getBytes("UTF-8"));  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
			return false;  
		} catch (UnsupportedEncodingException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}finally{  
			if(outputStream!=null){  
				try {  
					outputStream.flush();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
				try {  
					outputStream.close();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
			}  
		}  
		return true;

	} 
	/*
	 * read the file, and get the message history.
	 */
	public static  ArrayList<String> readTxtFile(String toid){

		ArrayList<String>data;
		data= new ArrayList<String>();
		String read;
		BufferedReader bufread;
		//String readStr ="";
		String filename="/sdcard/ChatHistory/"+uid+"/"+toid+".txt";
		FileReader fileread;
		try {
			fileread = new FileReader(filename);
			bufread = new BufferedReader(fileread);
			try {
				while ((read = bufread.readLine()) != null) {
					data.add(read);
					//readStr = readStr + read;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("文件内容是:"+ "\r\n" + readStr);
		return data;
	}


	/*
	 * delete the message from the history. 
	 */
	public static void delete(String toid){
		String path="/sdcard/ChatHistory/"+uid;
		String p = path+File.separator+toid+".txt";
		File file = new File(p);     
		if(!file.exists())
		{     

		}else
		{     
			if(file.isFile())
			{     
				file.delete();
			}



		}     
	}


/*
 * read the file, and return the string.
 */
	public static String  readTxtFile1(String toid){

		String output="";
		String read;
		BufferedReader bufread;
		//String readStr ="";
		String filename="/sdcard/ChatHistory/"+uid+"/"+toid+".txt";
		FileReader fileread;
		try {
			fileread = new FileReader(filename);
			bufread = new BufferedReader(fileread);
			try {
				while ((read = bufread.readLine()) != null) {
					if(read=="")
					{

					}
					else
					{
						output=output+read+"\r\n";;
					}
					//readStr = readStr + read;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("文件内容是:"+ "\r\n" + readStr);
		return output;
	}
	
	/*
	 * remove the one message from the history. 
	 */
	public void removeonemessage (ArrayList<String> result,String toid)
	{
		BufferedWriter bw = null;
		String newoutput="";

		for (int i=0;i<result.size();i++)
		{

				newoutput=newoutput+result.get(i)+"\r\n";
		}
		
		RandomAccessFile mm = null;
		//String filename="/sdcard/ChatHistory/"+uid+"/"+toid+".txt";
		try {
			bw = BufferedWriterFactory.create("/sdcard/ChatHistory/"+uid, toid+".txt");
			bw.write(newoutput);
		}
		catch (IOException e) {
			String exceptionMessage = toid+".txt" + " cannot be re-written.";
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (null != bw)
				bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	/*
	 * add the new message to the history. 
	 */

	public static void writeTxtFile(String newStr,String toid) throws IOException{


		String readStr=readTxtFile1(toid);
		String filein = readStr+newStr+"\r\n";
		RandomAccessFile mm = null;
		String filename="/sdcard/ChatHistory/"+uid+"/"+toid+".txt";
		try {
			mm = new RandomAccessFile(filename, "rw");
			mm.writeBytes(filein);
		} catch (IOException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		} finally {
			if (mm != null) {
				try {
					mm.close();
				} catch (IOException e2) {
					// TODO 自动生成 catch 块
					e2.printStackTrace();
				}
			}
		}
	}

}
