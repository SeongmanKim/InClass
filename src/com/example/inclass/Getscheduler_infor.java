
/**
 * this class is for get class and event information 
 * last change time: 3.3
 * @author yushao
 *
 */
package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.mysql.jdbc.ResultSet;

public class Getscheduler_infor {
	
	String uid;
	mysql ms;
	private ArrayList<ClassInfo> Classinfolist;
	private ArrayList<EventInfo> Eventinfolist;
	

	public  Getscheduler_infor(String uid, mysql ms,Context context) {
		
			
			this.uid=uid;
			this.ms=ms;
	

			Classinfolist= new ArrayList<ClassInfo>();
			Eventinfolist= new ArrayList<EventInfo>();
			getinfor();
	}
	
	
	
	public ArrayList<ClassInfo> getClasses(){
		//classes = takingClasses;
		return Classinfolist;
	}
	
	public ArrayList<EventInfo> getEvents(){
		//classes = takingClasses;
		return Eventinfolist;
	}
	
  
	
	public void getinfor()
	{

		Thread thread = new Thread()
		{  
            public void run(){  
		try 
		{
			
			
			ResultSet rs=null;
			String sql="select * from senior_project.Course where cid in (select cid from senior_project.Taking where uid = '"+uid+"')";
			rs=ms.execQuery(sql);
			while(rs.next())
			{
				ClassInfo c= new ClassInfo();
				
				String cname;
				cname=(String) rs.getObject("cname");
				String cnumber;
				cnumber=(String) (rs.getObject("number")+"");
				String day=(String)rs.getObject("date");
				String startTime=(String)rs.getObject("stime");
				String endTime=(String)rs.getObject("etime");
				String cid=(String)(rs.getObject("cid")+"");
				String professor=(String)(rs.getObject("professor")+"");
				String location =(String)(rs.getObject("location")+"");
				c.setName(cname);
				c.setCid(cid);
				c.setDay(day);
				c.setClassNumber(cnumber);
				c.setTime(startTime, endTime);
				c.setSection((String)(rs.getObject("section")+""));
				c.setDepartment((String) rs.getObject("department"));
				c.setProfessor(professor);
				c.setLocation(location);
				Classinfolist.add(c);
			}
			
			
			rs=null;
			///NEEED CHANGE THIS LINE FOR DB
			sql="select * from senior_project.Event where eid in (select eid from senior_project.Event_taking where uid = '"+uid+"')";
			rs=ms.execQuery(sql);
			while(rs.next())
			{
				EventInfo e= new EventInfo();
				
				String name;
				String eid=(String)(rs.getObject("eid")+"");
				name=(String) rs.getObject("name");
				//String cnumber;
				//cnumber=(String) (rs.getObject("number")+"");
				String day=(String)rs.getObject("day");
				String startTime=(String)rs.getObject("stime");
				String endTime=(String)rs.getObject("etime");
				String startDate=(String)rs.getObject("sdate");
				String endDate=(String)rs.getObject("edate");
				String location=(String)rs.getObject("location");
				
				e.setName(name);
				e.setEid(eid);
				e.setDay(day);
				//c.setClassNumber(cnumber);
				e.setTime(startTime, endTime);
				e.setDay(startDate, endDate);
				e.setLocation(location);
				//c.setSection((String)(rs.getObject("section")+""));
				//c.setDepartment((String) rs.getObject("department"));
				Eventinfolist.add(e);
			}
			
		   
            
		
		}
			
			catch (SQLException e)
		{

		}
       }
		};
		
		
		thread.start();  
        try{  
        	Thread.sleep(2000);
            
        } catch (InterruptedException e) {  
             
        }  
	}
	
	


}
