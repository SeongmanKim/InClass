package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSet;
/**
 * 
 * @author klyss_000
 *
 */
public class TakingE_eventSide {

	// class id
	private String eid;
	// all students
	private EventInfo els;
	private ArrayList<String> students;
	public TakingE_eventSide(String _eid){
		eid = _eid;
		els = new EventInfo();
		students = new ArrayList<String>();
	}

	public EventInfo setEvent(final mysql ms){
		Thread temp =new Thread() 
		{ 
			public void run()
			{
				try {
					ResultSet results=null;
					//NEED CHANGE THIS LINE FOR DB//
					String query="select * from senior_project.Event where eid='" + eid + "'";
					results=ms.execQuery(query);
					EventInfo returnEls = new EventInfo();	
					while(results.next()){
						returnEls.setEid(""+results.getInt("eid"));
						returnEls.setName(results.getString("name"));
						//returnCls.setClassNumber(""+results.getString("number"));
						returnEls.setDay(results.getString("day"));
						//returnCls.setSection(""+results.getInt("section"));
						returnEls.setTime(results.getString("stime"), results.getString("etime"));
						returnEls.setDay(results.getString("sdate"), results.getString("edate"));
						returnEls.setLocation(results.getString("location"));
						//returnCls.setProfessor(results.getString("professor"));
						//returnCls.setLocation(results.getString("location"));
						//returnCls.setDepartment(results.getString("department"));
					}
					els = returnEls;
					//NEED CHANGE THIS LINE TOO 
					query = "select realname from senior_project.User where uid in (select uid from senior_project.Event_taking where eid='"+eid+"')";
					results = ms.execQuery(query);
					while(results.next()){
						students.add(results.getString("realname"));
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
		return els;
	}

	public EventInfo getEventInfo(){
		return els;
	}
	
	public ArrayList<String> getStudents(){
		return students;
	}

}
