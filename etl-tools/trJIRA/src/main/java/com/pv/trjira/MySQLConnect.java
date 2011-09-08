package com.pv.trjira;

import java.sql.*;

public class MySQLConnect {

	Connection conn = null;

	public Connection getConnection() {

		if (this.conn == null) {
			try {
				String userName = JFDDSync.dbUsername;
				String password = JFDDSync.dbPassword;
				String url = JFDDSync.dbURL;
				Class.forName(JFDDSync.dbDriver).newInstance();
				this.conn = DriverManager.getConnection(url, userName, password);

			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		return conn;
	}

}