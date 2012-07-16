package com.dylanvivi.dao.support;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dylanvivi.dao.IBaseDao;

public class IBaseDaoSupport extends JdbcTemplate implements IBaseDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;  
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public <T> int save(Class<T> clazz) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> int saveOrUpdate(Class<T> clazz) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> int update(Class<T> clazz) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> int delete(T id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> void deleteObject(Class<T> clazz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T findById(Class<T> clazz, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> int countAll(Class<T> clazz) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> List<T> listAll(Class<T> clazz, String sql, Object... args) {
		return jdbcTemplate.queryForList(sql, clazz,args);
	}

	@Override
	public List<Map<String, Object>> queryList(String sql, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String sql,Object...args ) {
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, args);
		if (result != null && result.size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> boolean exists(Class<T> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
