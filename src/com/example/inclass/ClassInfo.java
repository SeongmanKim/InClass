/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * @author k3693692000.
 * CS4500 Senior Project
 * 
 * Class Information 
 * 
 * Has basic setter and getter for all information.
 */
package com.example.inclass;

import java.sql.Date;
import java.util.ArrayList;

import android.text.format.Time;


public class ClassInfo {
	// All information about a class.
	private String name;
	private String classNumber;
	private String cid;
	private String day;
	private String startTime;
	private String endTime;
	private String professor;
	private String location;
	private String section;
	private String department;
	private ArrayList<String> students;
	private String startDay;
	private String endDay;
	private String studentnumber;

	
	public ClassInfo(){
		name = null;
		classNumber = null;
		cid = null;
		day = null;
		startTime = null;
		endTime = null;
		professor = null;
		location = null;
		section = null;
		department = null;
		students = new ArrayList<String>();
		startDay = null;
		endDay = null;
		studentnumber = "";
	}
	void setDepartment(String _department){
		department = _department;
	}
	String getDepartment(){
		return department;
	}
	void setClassNumber(String _classNumber){
		classNumber = _classNumber;
	}
	void setSection(String _section){
		section = _section;
	}
	String getSection(){
		return section;
	}
	void setProfessor(String _professor){
		professor = _professor;
	}
	void setLocation (String _location){
		location = _location;
	}
	void setName(String _name){
		name = _name; 
	}
	void setCid(String _cid){
		cid = _cid;
	}
	void setDay(String _day){
		day = _day;
	}
	
	void setDays(String _startDay, String _endDay)
	{
		startDay = _startDay;
		endDay = _endDay;
	}
	void setTime(String _startTime, String _endTime){
		startTime = _startTime;
		endTime = _endTime;
	}
	
	void setStudentNumber(String _studentNumber){
		studentnumber = _studentNumber;
	}
	
	public String getStudentNumber(){
		return studentnumber;
	}
	public String getProfessor(){
		return professor;
	}
	public String getLocation(){
		return location;
	}
	public String getName(){
		return name;
	}
	public String getCid(){
		return cid;
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
	public String getClassNumber(){
		return classNumber;
	}
	public ArrayList<String> getStudents(){
		return students;
	}
	public String getClassNumber_()
	{
		return department+classNumber;
		
	}
	
	public String getStartDay()
	{
		return startDay;
	}
	public String getEndDay()
	{
		return endDay;
	}
}
