package com.example.inclass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

/**
 * This class will connect with eventinformation paage
 * Young Soo Choi
 *
 */
public class EventInformationPage extends View{
	private EventInfo els;
	//includes all students (only id)
	// it will also have linking
	//private ArrayList<String> studentIDs;
	private ArrayList<String> students;
	private TakingE_eventSide taking;
	mysql ms;
	String eid;
	//Cuntstuctor
	public EventInformationPage(Context context, mysql _ms, String _eid) throws SQLException {
		super(context);
		// TODO Auto-generated constructor stub
		// actual code 

		ms = _ms;
		eid = _eid;
		taking = new TakingE_eventSide(eid);
		// test purpse.
		if(eid.equals("")){
			els = new EventInfo();
		}
		else
			els = setupEventInfo();
	}
	//return classes from DB
	private EventInfo setupEventInfo() throws SQLException {
		// TODO Auto-generated method stub
		
		return taking.setEvent(ms);
	}
	//retrun class information
	public EventInfo returneventinfo()
	{
		return els;
	}
	//Return class's student list from each class
	public ArrayList<String> getstudents()
	{
		students = taking.getStudents();
		return students;
	}
	

	





}
