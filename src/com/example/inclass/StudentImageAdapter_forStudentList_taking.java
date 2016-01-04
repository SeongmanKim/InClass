package com.example.inclass;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Simple adapter that takes hashmap of userId and URL of their images.
 * @author k3693692000
 * 
 * Used MIT library.
 *
 */
public class StudentImageAdapter_forStudentList_taking extends BaseAdapter{

	private LayoutInflater mInflater;
	Context context;
	ArrayList<ArrayList<String>> students;
	ImageView photo;
	Intent intent;
	String myuid;
	public StudentImageAdapter_forStudentList_taking(Context context, String myuid) {  
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.myuid = myuid;
		students = new ArrayList<ArrayList<String>>();
		photo = new ImageView(context);
		this.intent = new Intent(this.context, UserInformationActivity.class);
	}
	public void setDataSet(ArrayList<ArrayList<String>> students){
		this.students =students;  
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return students.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Context newContext = context;
		convertView = mInflater.inflate(R.layout.student_image_xml_50_60 , null);
		TextView uid= (TextView)convertView.findViewById(R.id.student_text_button);
		final String targetUid = students.get(position).get(0);
		uid.setText(targetUid);
		photo = (ImageView)convertView.findViewById(R.id.student_image_button);
		
		if(students.get(position).get(1)!=null)
		{
			ImageLoader imageLoader=new ImageLoader(newContext);
			imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+students.get(position).get(1),photo);
		}

		photo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				intent.putExtra("targetUid", targetUid);
				intent.putExtra("userId", myuid);
				context.startActivity(intent);

			}
		});



		return convertView;
	}

}
