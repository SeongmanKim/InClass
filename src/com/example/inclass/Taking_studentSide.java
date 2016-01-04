/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 * @author k3693692000
 * user information and classes that the user is taking
 * 
 */
package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSet;

public class Taking_studentSide {

	// class id
		private String userName;
		private String uid;
		// all students
		private ArrayList<ClassInfo> Classinfolist;
		
		public Taking_studentSide(String _uid,String _userName){
			//userName = _userName;
			//classes = _classes;
			uid=_uid+"";
			userName=_userName;
			Classinfolist= new ArrayList<ClassInfo>();
		}
		
		public void getClasses(final mysql ms){
			Classinfolist= new ArrayList<ClassInfo>();
			
			//Classinfolist.clear();
			Thread temp =new Thread() 
			{ 
				public void run()
				{
					try {
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
							String startDate=(String)rs.getObject("sdate");
							String endDate=(String)rs.getObject("edate");
							String pro=(String)rs.getObject("professor");
							String location=(String)rs.getObject("location");
							c.setName(cname);
							c.setCid(cid);
							c.setDay(day);
							c.setClassNumber(cnumber);
							c.setTime(startTime, endTime);
							c.setDays(startDate, endDate);
							c.setSection((String)(rs.getObject("section")+""));
							c.setDepartment((String) rs.getObject("department"));
							c.setProfessor(pro);
							c.setLocation(location);
							Classinfolist.add(c);
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
		
		public ArrayList<ClassInfo> getClasses(){
			//classes = takingClasses;
			return Classinfolist;
		}
		
}
