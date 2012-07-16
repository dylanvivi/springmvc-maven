package com.dylanvivi.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成javaBean实体类
 * Oracle版本
 * @author dylan
 * 2012.03.07  
 */
public class Build extends DataBase{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Build b = new Build();
		b.build();
	}

	/**
	 * 自动生成JAVABEAN实体类
	 */
	public void build(){
		// oracle
		String sql = "select table_name from user_tables where table_name = 'DT_XMS_CLIENTS'";
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
