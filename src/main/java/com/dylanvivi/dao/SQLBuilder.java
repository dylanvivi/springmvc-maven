package com.dylanvivi.dao;

import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SQLBuilder {

	private final static Log log = LogFactory.getLog(SQLBuilder.class);

	public static Object[] insertSQL(Object obj) {
		String tablename = TableProcessor.getTableName(obj);
		StringBuffer colSB = new StringBuffer();
		StringBuffer parSB = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		try{
		List<Field> fields = TableProcessor.findAllField(obj);
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			colSB.append(TableProcessor.getFieldName(field));
			parSB.append("?");
			if (i < fields.size() - 1) {
				colSB.append(",");
				parSB.append(",");
			}
			param.add(field.get(obj));
		}
		}catch(IllegalAccessException e){
			log.error("IllegalAccessException", e);
			return null;
		}
		String insert = "insert into " + tablename + " (" + colSB.toString()
				+ " ) " + " values (" + parSB.toString() + ")";
		log.debug("insertSQL : " + insert);

		return new Object[] { insert, param };
	}

	// /id...
	public static Object[] updateSQL(Object obj){
		String tablename = TableProcessor.getTableName(obj);

		StringBuffer colSB = new StringBuffer();
		StringBuffer parSB = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		try{
			List<Field> fields = TableProcessor.findAllField(obj);
			
			for (int i = 0; i < fields.size(); i++) {
				Field field = fields.get(i);
				colSB.append(TableProcessor.getFieldName(field));
				colSB.append(" = ?");
				if (i < fields.size() - 1) {
					colSB.append(",");
				}
				param.add(field.get(obj));
			}
			// 拿到主键列表
			List<Field> primaryKeys = TableProcessor.getAllPrimaryKeys(obj
					.getClass());
			// 如果没有主键
			if (primaryKeys.isEmpty()) {
				throw new IllegalArgumentException("key value for table '"
						+ tablename + "' cannot be null.");
			} else {
				for (int i = 0; i < primaryKeys.size(); i++) {
					Field field = primaryKeys.get(i);
					parSB.append(TableProcessor.getFieldName(field));
					parSB.append(" = ?");
					if (i < primaryKeys.size() - 1) {
						parSB.append(" AND ");
					}
					param.add(field.get(obj));
				}
			}
		}catch(IllegalAccessException e){
			log.error("Failed to get value",e);
		}
		String update = "UPDATE " + tablename + " SET " + colSB.toString() + " "
				+ "WHERE " + parSB.toString() + "";
		log.debug("updateSQL : " + update);

		return new Object[] { update, param };

	}

	public static Object[] deleteSQL(Object obj){
		String tablename = TableProcessor.getTableName(obj);
		StringBuffer parSB = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		// 拿到主键列表
		try{
		List<Field> primaryKeys = TableProcessor.getAllPrimaryKeys(obj
				.getClass());
		if (primaryKeys.isEmpty()) {
			throw new IllegalArgumentException("key value for table '"
					+ tablename + "' cannot be null.");
		} else {
			for (int i = 0; i < primaryKeys.size(); i++) {
				Field field = primaryKeys.get(i);
				parSB.append(TableProcessor.getFieldName(field));
				parSB.append(" = ?");
				if (i < primaryKeys.size() - 1) {
					parSB.append(" AND ");
				}
				param.add(field.get(obj));
			}
		}
		}catch(IllegalAccessException e){
			log.error("Failed to get value",e);
		}

		String delete = "DELETE FROM " + tablename + " WHERE "
				+ parSB.toString();
		log.debug("deleteSQL : " + delete);
		return new Object[] { delete, param.toArray() };
	}
	
	public static String findByIdSql(Class<?> obj) {
		String tableName = TableProcessor.getTableName(obj);
		List<Field> primaryKeys = TableProcessor.getAllPrimaryKeys(obj);

		StringBuffer sql = new StringBuffer();

		sql.append("select * from ");
		sql.append(tableName);
		sql.append("where ");
		
		for(int i = 0; i < primaryKeys.size(); i++){
			Field field = primaryKeys.get(i);
			sql.append(TableProcessor.getFieldName(field) + "= ? ");
			if(i < primaryKeys.size() -1){
				sql.append("and");
			}

		}
		log.debug("genarte findById SQL: " + sql.toString());
		return sql.toString();

	}
}
