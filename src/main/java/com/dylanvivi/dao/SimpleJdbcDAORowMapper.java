package com.dylanvivi.dao;

import java.lang.reflect.Field;
import java.sql.ResultSet;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;


public class SimpleJdbcDAORowMapper<T> extends ParameterizedBeanPropertyRowMapper<T>{
	private SimpleJdbcDAORowMapper() {
	}
	
	public static <T> SimpleJdbcDAORowMapper<T> newInstance(Class<T> clazz) {
		SimpleJdbcDAORowMapper<T> newInstance = new SimpleJdbcDAORowMapper<T>();
		newInstance.setMappedClass(clazz);
		return newInstance;
	}
	
	@Override
	public T mapRow(ResultSet rs, int rowNumber) {
		try{
			T obj = getMappedClass().newInstance();
			Field[] fields = obj.getClass().getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				BeanProcessor.typeMapper(rs, obj, field);
				field.setAccessible(false);
			}
			return obj;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
