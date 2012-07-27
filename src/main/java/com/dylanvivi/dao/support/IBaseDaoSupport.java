package com.dylanvivi.dao.support;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.dylanvivi.dao.IBaseDao;
import com.dylanvivi.dao.SQLBuilder;

import com.dylanvivi.dao.SimpleJdbcDAORowMapper;
import com.dylanvivi.util.StringUtils;

public class IBaseDaoSupport extends JdbcTemplate implements IBaseDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;  
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int save(Object obj) {
		Object[] param = SQLBuilder.insertSQL(obj);
		int count = getJdbcTemplate().update((String)param[0], (Object[])param[1]);
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
		int count = getJdbcTemplate().update((String)param[0],(Object[])param[1]);
		return count;
	}

	@Override
	public <T> int deleteObject(Object obj) {
		Object[] param = SQLBuilder.deleteSQL(obj);
		int count = getJdbcTemplate().update((String)param[0], (Object[])param[1]);
		return count;
		
	}

	@Override
	public <T> T findById(Class<T> clazz, Object... args) {
		String sql = SQLBuilder.findByIdSql(clazz);
		List<T> results = getJdbcTemplate().query(sql, args,
				new RowMapperResultSetExtractor<T>(SimpleJdbcDAORowMapper.newInstance(clazz), 1));
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public <T> int countAll(Class<T> clazz) {
		String sql = SQLBuilder.countAll(clazz);
		if(!StringUtils.isEmpty(sql)){
			return getJdbcTemplate().queryForInt(sql);
		}
		return 0;
	}
	
	@Override
	public int queryForInt(String sql,Object... args){
		return getJdbcTemplate().queryForInt(sql,args);
	}
	
	@Override
	public int countAll(String sql,Object... args) {
		return getJdbcTemplate().queryForInt(sql,args);
	}

	@Override
	public <T> List<T> listAll(Class<T> clazz, String sql, Object... args) {
		return getJdbcTemplate().queryForList(sql, clazz,args);
	}

	@Override
	public List<Map<String, Object>> queryList(String sql, Object... args) {
		return getJdbcTemplate().queryForList(sql,args);
	}

	@Override
	public boolean exists(String sql,Object...args ) {
		List<Map<String, Object>> result = getJdbcTemplate().queryForList(sql, args);
		if (result != null && result.size() > 0)
			return true;
		else
			return false;
	}
	
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	
}
