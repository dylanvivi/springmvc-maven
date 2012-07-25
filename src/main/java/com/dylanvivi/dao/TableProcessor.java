package com.dylanvivi.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dylanvivi.dao.annotation.Column;
import com.dylanvivi.dao.annotation.Id;
import com.dylanvivi.dao.annotation.Table;

public class TableProcessor {
	
	//拿到当前行的名字
	public static String getFieldName(Field field){
		String fieldName = field.getName();
		Column col = field.getAnnotation(Column.class);
		if(col != null){
			fieldName = col == null ? fieldName : col.name();
		}else{
			return null;
		}
		return fieldName;
		
	}
	
	public static String getTableName(Object obj){
		String tablename = null;
		Class<?> clazz = null;
		if(obj instanceof Class){
			clazz = (Class<?>) obj;
		}else{
			clazz = obj.getClass();
		}
		Table table = clazz.getAnnotation(Table.class);
		tablename = table.name();
		return tablename;
	}
	
	public static List<Field> findAllField(Object obj){
		Class<?> cl = obj.getClass();
		List<Field> list = new ArrayList<Field>();
		
		Field[] fs = cl.getDeclaredFields();
		try{
			for(Field field: fs){
				field.setAccessible(true); //这个设置可以访问私有变量
				if(field.getAnnotation(Column.class)!=null && field.get(obj)!=null){
					list.add(field);
				}
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		return list;
		
	}
	
	public static List<Field> getAllPrimaryKeys(Class<?> clazz){
		List<Field> list = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			if(field.getAnnotation(Id.class) != null ){
				list.add(field);
			}
		}
		return list;
	}
	
	public static void typeMapper(ResultSet rs, Object obj, Field field) throws IllegalArgumentException, IllegalAccessException, SQLException{
		Class<?> clz = field.getClass();
		String fieldName = getFieldName(field);
		if(clz.equals(String.class)){
			field.set(obj, rs.getString(fieldName));
		}else if(clz.equals(Integer.class)){
			field.set(obj, rs.getInt(fieldName));
		}else if(clz.equals(Double.class)){
			field.set(obj, rs.getDouble(fieldName));
		}else if(clz.equals(Float.class)){
			field.set(obj, rs.getFloat(fieldName));
		}else if(clz.equals(Date.class)){
			field.set(obj, rs.getTimestamp(fieldName));
		}else if(clz.equals(Byte.class)){
			field.set(obj, rs.getByte(fieldName));
		}else if(clz.equals(Boolean.class)){
			field.set(obj, rs.getBoolean(fieldName));
		}else if(clz.equals(Object.class)){
			field.set(obj, rs.getObject(fieldName));
		}
	}
	
	public static <T>Object resutSetMapper(ResultSet rs,Object obj,Class<T> clazz){
		return null;
	}
	

}
