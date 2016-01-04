package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.mysql.jdbc.ResultSet;
/**
 * 
 * @author klyss_000, k3693692000 (add up parcelable)
 * 
 * 
 *
 */
public class TakingE_studentSide{

	// class id
	private String userName;
	private String uid;
	// all students
	private ArrayList<EventInfo> Eventinfolist;

	public TakingE_studentSide(String _uid,String _userName){
		//userName = _userName;
		//classes = _classes;
		uid=_uid+"";
		userName=_userName;
		Eventinfolist= new ArrayList<EventInfo>();
	}

	public void getEvents(final mysql ms){
		Thread temp =new Thread() 
		{ 
			public void run()
			{
				try {
					ResultSet rs=null;
					///NEEED CHANGE THIS LINE FOR DB
					String sql="select * from senior_project.Event where eid in (select eid from senior_project.Event_taking where uid = '"+uid+"')";
					rs=ms.execQuery(sql);
					Eventinfolist = new ArrayList<EventInfo>();
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

		//return classes;
	}

	public ArrayList<EventInfo> getEvents(){
		//classes = takingClasses;
		return Eventinfolist;
	}


}
