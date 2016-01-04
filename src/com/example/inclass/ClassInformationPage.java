/**
f * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 * Has all class information page.
 * 
 */
package com.example.inclass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class ClassInformationPage extends View{
	private ClassInfo cls;
	//includes all students (only id)
	// it will also have linking
	private ArrayList<String> students;
	private Taking_classSide taking;
	private ArrayList<ArrayList<String>> students_Image;
	
	mysql ms;
	String cid;
	public ClassInformationPage(Context context, mysql _ms, String _cid) throws SQLException {
		super(context);
		// TODO Auto-generated constructor stub
		// actual code 

		ms = _ms;
		cid = _cid;
		taking = new Taking_classSide(cid);
		students_Image = new ArrayList<ArrayList<String>>();
		// test purpse.
		if(cid.equals("")){
			cls = new ClassInfo();
		}
		else{
			cls = setupClassInfo();
			students_Image = getStudents_Image(ms);
		}
		
	}
	
	private ClassInfo setupClassInfo() throws SQLException {
		// TODO Auto-generated method stub
		return taking.setClass(ms);
	}
	
	private ArrayList<ArrayList<String>> getStudents_Image(mysql ms){
		
		return taking.getStudentsImage();
	}

	public ClassInfo returnclassinfo()
	{
		return cls;
	}

	public ArrayList<String> getstudents()
	{
		students = taking.getStudents();
		return students;
	}
	
	public ArrayList<String> getstudentids(){
		return taking.getstudentids();
	}
	public ArrayList<ArrayList<String>> getStudentsImage(){
		return students_Image;
	}
}
