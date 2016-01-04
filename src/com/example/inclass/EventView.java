package com.example.inclass;

import java.util.ArrayList;
import java.util.Random;

import android.R.drawable;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

/**
 * This class is connection between DB and JAVA 
 * This class will delete soon.
 * @author Young Soo Choi
 *
 */
public class EventView {

	
	int eventInfoNumber;
	boolean clicked;
	public String day;
	public String startTime;
	public String finTime;
	public String name;
	public String sec;
	public String eid;
	public int top;
	public int bottom;
	

	ArrayList<EventInfo> eventinfo;
	Button btn;

	public EventView(Context context) {

		clicked = false;

		eventInfoNumber = 0;
		btn = new Button(context);
		day = null;
		startTime = null;
		finTime = null;
		name =null;
		sec = null;
		eid = null;
		top = 0;
		bottom = 0;

		// TODO Auto-generated constructor stub
	}
	
	
	//Add list from DB
	public void addList(ArrayList<EventInfo> _eventinfo)
	{
		eventinfo=_eventinfo;
		
	}
	//Get info from list
	public ArrayList getEventInfos(){
		return eventinfo;
	}

}