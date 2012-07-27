package com.dylanvivi.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成javaBean实体类
 * Mysql版本
 * @author dylan
 * 2012.03.07  
 */
public class BuildMySQL extends DataBase{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BuildMySQL b = new BuildMySQL();
		b.build();
	}

	/**
	 * 自动生成JAVABEAN实体类
	 */
	public void build(){
		// oracle
		String sql = "select table_name from information_schema.TABLES where TABLE_SCHEMA = 'test'";
		BuildDTO dto = new BuildDTO();
		List list = new ArrayList();
		try {
			this.conn = this.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				String tname = rs.getString(1);
				if(!tname.equals("dtproperties")){
					list.add(tname);
				}
			}
			this.closeAll();
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<list.size();i++){
			String tname = (String)list.get(i);
			System.out.println(tname);
			dto.build(tname.toUpperCase());
		}
	}
}
