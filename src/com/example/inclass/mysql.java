/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * mysql class connect the database. 
 * @author yushao 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.sql.DriverManager;

import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class mysql {
	public    Connection conn = null;
	public    Statement stmt = null;
	public    ResultSet rs = null;
	//public boolean c=false;
/*
 * connect the data base. 
 */
	public void connectDB()
	{        


				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://senior-07.eng.utah.edu";
					conn = (Connection) DriverManager.getConnection(url, "shaoyu", "shaoyu");
					Log.i("MySQL", "Internet Connected");
				}catch (Exception e){
				} 
	}
/*
 * close the connection 
 */
	public boolean closeConn() throws java.sql.SQLException {
		try{
			if(stmt != null){
				stmt.close();
				stmt = null;
			}

			if(conn != null){
				conn.close();
				conn = null;
			}
			return true;
		}catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
/*
 * execte the query. 
 */
	public ResultSet execQuery(String sql) throws java.sql.SQLException {
		
		rs = null;
		if (conn != null && sql != null) {
			try{
				stmt = (Statement) conn.createStatement();
				rs = (ResultSet) stmt.executeQuery(sql);
                 
			}catch(SQLException ex)       
			{
				ex.printStackTrace();
			}
		}
		return rs;
	}
/*
 * execte the updata query. 
 */
	public int execUpdate(String sql) throws java.sql.SQLException {
		int rows = 0;

		if (conn != null && sql != null){
			try{
				stmt = (Statement) conn.createStatement();
				rows = stmt.executeUpdate(sql);
			}catch(SQLException ex)        {
				ex.printStackTrace();
			}
		}
		return rows;
	}
}
