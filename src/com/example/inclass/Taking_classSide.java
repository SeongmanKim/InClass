/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * people taking a class.
 * 
 * @author k3693692000
 * 
 * information about class and students.
 */
package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.widget.ImageButton;

import com.mysql.jdbc.ResultSet;

public class Taking_classSide {

	// class id
	private String cid;
	// all students
	private ClassInfo cls;
	private ArrayList<String> students;
	private ArrayList<String> studentids;
	private ArrayList<ArrayList<String>> students_Image;
	mysql ms;
	public Taking_classSide(String _cid){
		cid = _cid;
		cls = new ClassInfo();
		students = new ArrayList<String>();
		students_Image = new ArrayList<ArrayList<String>>();
		studentids= new ArrayList<String>();
		ms = new mysql();
	}
	public ClassInfo setClass(final mysql ms){
		this.ms=ms;
		Thread temp =new Thread() 
		{ 
			public void run()
			{
				try {
					ResultSet results=null;
					
					String query="SELECT *,(select count(*) from senior_project.Taking where cid = '" + cid +"') as count FROM senior_project.Course where cid = '" + cid +"';";
					results=ms.execQuery(query);
					ClassInfo returnCls = new ClassInfo();	
					while(results.next()){
						returnCls.setCid(""+results.getInt("cid"));
						returnCls.setName(results.getString("cname"));
						returnCls.setClassNumber(""+results.getString("number"));
						returnCls.setDay(results.getString("date"));
						returnCls.setSection(""+results.getInt("section"));
						returnCls.setTime(results.getString("stime"), results.getString("etime"));
						returnCls.setDays(results.getString("sdate"), results.getString("edate"));
						returnCls.setProfessor(results.getString("professor"));
						returnCls.setLocation(results.getString("location"));
						returnCls.setDepartment(results.getString("department"));
						returnCls.setStudentNumber(results.getString("count"));
					}
					cls = returnCls;
					query = "select realname,uid from senior_project.User where uid in (select uid from senior_project.Taking where cid='"+cid+"')";
					results = ms.execQuery(query);
					while(results.next()){
						students.add(results.getString("realname"));
						studentids.add(results.getString("uid"));
					}
					setStudentsImage();
					
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
		return cls;
	}
	
	public ArrayList<String> getstudentids(){
		return studentids;
	}
	public ClassInfo getClassInfo(){
		return cls;
	}
	public ArrayList<ArrayList<String>> getStudentsImage(){
		return students_Image;
	}
	
	public ArrayList<String> getStudents(){
		return students;
	}
	
	private void setStudentsImage() {
		// TODO Auto-generated method stub
		//ms.connectDB();
		String sql="select uid, photo from senior_project.User where uid in (SELECT uid FROM senior_project.Taking where cid='"+cid+"')";
		//ResultSet rs=null;
		try {
			ResultSet rs=null;
			rs=ms.execQuery(sql);
			while(rs.next())
			{ 
				//ImageButton each_student_Image = (ImageButton) findViewById(R.id.student_image_button);
				String content=rs.getString("uid");
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(rs.getString("uid"));
				temp.add(rs.getString("photo"));
				students_Image.add(temp);
			}
			System.out.println("");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
