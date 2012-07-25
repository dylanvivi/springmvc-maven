package com.dylanvivi.dao.support;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dylanvivi.dao.IBaseDao;
import com.dylanvivi.dao.SQLBuilder;
import com.dylanvivi.dao.TableProcessor;

public class IBaseDaoSupport extends JdbcTemplate implements IBaseDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;  
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public <T> int save(Object obj) {
		Object[] param = SQLBuilder.insertSQL(obj);
		int count = jdbcTemplate.update((String)param[0], param[1]);
		return count;
	}

	@Override
	public <T> int saveOrUpdate(Object obj) {
		int count = update(obj);
		if(count == 0){
			count = save(obj);
		}
		return count;
	}

	@Override
	public <T> int update(Object obj) {
		Object[] param = SQLBuilder.updateSQL(obj);
		int count = getJdbcTemplate().update((String)param[0],param[1]);
		return count;
	}

	@Override
	public <T> int delete(T id) {
		
		return 0;
	}

	@Override
	public <T> int deleteObject(Object obj) {
		Object[] param = SQLBuilder.deleteSQL(obj);
		int count = getJdbcTemplate().update((String)param[0], param[1]);
		return count;
		
	}

	@Override
	public <T> T findById(Class<T> clazz, Object... args) {
		String sql = SQLBuilder.findByIdSql(clazz);
		
		return null;
	}

	@Override
	public <T> int countAll(Class<T> clazz) {
		
		return 0;
	}
	
	@Override
	public int queryForInt(String sql,Object... args){
		return jdbcTemplate.queryForInt(sql,args);
	}
	
	@Override
	public int countAll(String sql,Object... args) {
		return jdbcTemplate.queryForInt(sql,args);
	}

	@Override
	public <T> List<T> listAll(Class<T> clazz, String sql, Object... args) {
		return jdbcTemplate.queryForList(sql, clazz,args);
	}

	@Override
	public List<Map<String, Object>> queryList(String sql, Object... args) {
		return jdbcTemplate.queryForList(sql,args);
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
	public <T> boolean exists(Class<T> clazz) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	
}
