package com.soongwei.meow.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DatabaseConnection {
	final static Logger logger = Logger.getLogger(DatabaseConnection.class);

	public static String ipAddress = "tcmhq-mam-02.tcm.tc.net";
	public static String port = "1433";
	public static String databaseName = "Attendance";
	public static String username = "MAM";
	public static String password = "P@ssw0rd";

	public static String getURL() {
		return "jdbc:sqlserver://" + ipAddress + ";databaseName=" + databaseName
				+ ";sendStringParametersAsUnicode=false;prepareSQL=0";
	}

	public static Properties getInfo() {
		Properties info = new Properties();
		info.put("username", username);
		info.put("password", password);
		return info;
	}

	public static Connection getConnection() {
		Connection connection = null;

		String url = "jdbc:sqlserver://" + ipAddress + "\\SQLEXPRESS:" + port + ";databaseName=" + databaseName + "";

		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String databaseUserName = username;
		String databasePassword = password;

		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url, databaseUserName, databasePassword);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			logger.error("Error getConnection()", e);
		}

		return connection;
	}

}
