package com.dylanvivi.util;



import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;




public class DataBase {

	//	Logger logger = Logger.getLogger(DBUtil.class.getName());
	private static  String DRIVER ;
	private static  String URL ;
	private static String USER ;
	private  static String PASS;

	public Connection conn;
	public PreparedStatement pstmt;
	public ResultSet rs;
	public CallableStatement cstmt;


	public Connection getConn() throws ClassNotFoundException{
		Connection conn = null;
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream("/datasource.properties"));
			DRIVER = properties.getProperty("jdbc.driverClass");
			URL = properties.getProperty("jdbc.jdbcUrl");
			USER = properties.getProperty("jdbc.user");
			PASS = properties.getProperty("jdbc.password");
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL,USER,PASS);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}


	public void closeAll(){
		try {
			if(rs !=null){
				rs.close();
				rs = null;
			}
			if(pstmt!=null){
				pstmt.close();
				pstmt = null;
			}
			if(conn!=null){
				conn.close();
				conn=null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public int execulQ(String sql,String[] param){
		int num = 0;
		try {
			conn = this.getConn();
			pstmt = conn.prepareStatement(sql);
			for(int i=0;i<param.length;i++){
				pstmt.setString(i+1, param[i]);
			}
			num = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
}
