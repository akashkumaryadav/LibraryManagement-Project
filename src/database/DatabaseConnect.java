package database;


import java.sql.*;

public class DatabaseConnect {
	
	//error of time zone with new driver com.mysql.cj.jdbc.Driver
	
	private static String timezone = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	
	//base url for working with localhost
	
	private static String baseurl = "jdbc:mysql://localhost/";
	
	public static String getTimezone() {
		return timezone;
	}

	public static String getBaseurl() {
		return baseurl;
	}
	
	public  DatabaseConnect() {
		
	}
	
	public static Connection establishConnection(String url,String uname,String pass) {
		
		Connection conn=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url,uname,pass);
		} catch (SQLException e) {
			System.out.println("inside Connect sql exception");
			e.printStackTrace();
		}	catch (ClassNotFoundException e) {
			System.out.println("Inside connect class not found exception");
			e.printStackTrace();
		}
		
		return conn;
	}
		
}




//jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
