package com.example.inclass;

import java.sql.Date;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

/**
 * This class will set Each event's variable
 * Young Soo Choi.
 *
 */

/**
 * Parcelable by Seongman Kim
 * @author k3693692000
 *
 */
public class EventInfo implements Parcelable{
	private String name;
	private String eid;
	private String day; //class day
	private String startTime;
	private String endTime;
	private String location;
	//private ArrayList<String> students;
	private String startDay;
	private String endDay;

	//Cunstructor
	public EventInfo(){
		name = null;
		eid = null;
		day = null;
		startTime = null;
		endTime = null;
		//students = new ArrayList<String>();
		startDay = null;
		endDay = null;
	}
	// Constructor for parcelable
	private EventInfo(Parcel in){
		name = in.readString();
		eid = in.readString();
		day = in.readString();
		startTime = in.readString();
		endTime = in.readString();
		
		//students = (ArrayList<String>) in.readSerializable();
		startDay = in.readString();
		endDay = in.readString();
	}
	
	//Set part
	void setLocation (String _location){
		location = _location;
	}
	void setName(String _name){
		name = _name; 
	}
	void setEid(String _eid){
		eid = _eid;
	}
	void setDay(String _day){
		day = _day;
	}
	void setTime(String _startTime, String _endTime){
		startTime = _startTime;
		endTime = _endTime;
	}
	void setDay(String _startDay, String _endDay)
	{
		startDay = _startDay;
		endDay = _endDay;
	}
	//Get part
	
	public String getLocation(){
		return location;
	}
	public String getName(){
		return name;
	}
	public String getEid(){
		return eid;
	}
	public String getDay(){
		return day;
	}
	public String getStartTime(){
		return startTime;
	}
	public String getEndTime(){
		return endTime;
	}
	
//	public ArrayList<String> getStudents(){
//		return students;
//	}
	
	public String getStartDay()
	{
		return startDay;
	}
	public String getEndDay()
	{
		return endDay;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(name);
		dest.writeString(eid);
		dest.writeString(day);
		dest.writeString(startTime);
		dest.writeString(endTime);
		dest.writeString(startDay);
		dest.writeString(endDay);
		//dest.writeSerializable(students);
	}
	
	public static final Parcelable.Creator<EventInfo> CREATOR = new Parcelable.Creator<EventInfo>() {
        public EventInfo createFromParcel(Parcel in) {
            return new EventInfo(in);
        }

        public EventInfo[] newArray(int size) {
            return new EventInfo[size];
        }
    };
	
	
}
