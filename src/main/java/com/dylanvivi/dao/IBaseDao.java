package com.dylanvivi.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;



public interface IBaseDao{
	/**
	 * 储存对象，返回插入条数
	 * @param model
	 * @return
	 */
    public <T>int save(Object obj);

    /**
     * 存在则更新，反之插入，返回记录条数
     * @param clazz
     * @return
     */
    public <T>int saveOrUpdate(Object obj);
    
    /**
     * 更新
     * @param clazz
     */
    public <T> int update(Object obj);
    
    /**
     * 根据sql更新
     * @param sql
     * @param args
     */
    public int update(String sql,Object... args);
    
    /**
	 * 取出返回集里的类型为Int的字段
	 * @param sql 
	 * @return
	 */
	public int queryForInt(String sql,Object... args);
    
    /**
     * 删除by Id
     * @param id
     * @return
     */
    public <T> int delete(T id);
    
    /**
     * 删除对象
     * @param clazz
     */
    public <T> int deleteObject(Object obj);

    /**
     * 根据id查找对象
     * @param id
     * @return
     */
    public <T> T findById(Class<T> clazz, Object... args);;
    
    /**
     * 返回记录条数
     * @param clazz
     * @return
     */
    public <T> int countAll(Class<T> clazz);
    
    /**
     * 根据sql查找所有
     * @param clazz
     * @param sql
     * @param args
     * @return
     */
    public <T> List<T> listAll(Class<T> clazz, String sql, Object... args);
    
    /**
	 * 返回查询结果map对象list
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql, Object... args);
	
	/**
	 * 返回列名-值的集合
	 * @param sql 查询语句
	 * @param args 
	 * @return
	 */
	public Map<String, Object> queryForMap(String sql, Object... args);

    
    /**
     * 检查对象是否存在
     * @param id
     * @return
     */
    public <T> boolean exists(Class<T> clazz);
    
    /**
     * 检查对象是否存在
     * @param id
     * @return
     */
    public boolean exists(String sql,Object...args );
    
    public int countAll(String sql,Object... args);
    
    public JdbcTemplate getJdbcTemplate();

}
