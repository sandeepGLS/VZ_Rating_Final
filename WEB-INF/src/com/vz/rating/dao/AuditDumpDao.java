package com.vz.rating.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.vz.rating.connection.MyConnection;

public class AuditDumpDao {

	Connection conn;
	PreparedStatement pstmt;
	
	public AuditDumpDao() {
		initalizeConnection("jdbc/vz");
	}


	public void initalizeConnection(String contextName){
		try {
			conn = new MyConnection().getConnection(contextName);
		} catch (Exception e) {
			System.out.println("Exception while initalizing connection in initalizeConnection() NotificationsDao");
			e.printStackTrace();
		}
	}
	
	public void closeAll(){
		
		try {
			if(pstmt!=null)
				pstmt.close();
			if(conn!=null)
				conn.close();
		} catch (Exception e) {
			System.out.println("Connections are closed in AuditDumpDao Class ");
			e.printStackTrace();
		}
	}

	
	public void insertData(String ipAddress,String actionName,String jsonParam){
		
		try {
			pstmt = conn.prepareStatement("insert into audit_dump values(?,?,?,now())");
			pstmt.setString(1, ipAddress);
			pstmt.setString(2, actionName);
			pstmt.setString(3, jsonParam);
			pstmt.executeUpdate();
			System.out.println(pstmt.toString());
		} catch (Exception e) {
			System.out.println("Error while inserting audit data !!!");
			e.printStackTrace();
		}
		
	}
	
}
