package com.vz.rating.connection;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MyConnection
{
	public Connection getConnection(String resource) 
		{
			Connection conn = null;
			try {
					Context initContext = new InitialContext();
					Context envContext = (Context) initContext.lookup("java:/comp/env");
					DataSource ds = (DataSource) envContext.lookup(resource);
					conn = ds.getConnection();				
				}
			catch (Exception e)
				{
					e.printStackTrace();
				}
			return conn;
	}
} // end of class
