package com.dylanvivi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dylanvivi.dao.IBaseDao;
import com.dylanvivi.pojo.User;
import com.dylanvivi.util.PasswordUtil;

@Service
public class TestService {
	
	@Autowired
	private IBaseDao dao;
	
	public boolean check(){
		String sql = "SELECT * FROM user";
		return dao.exists(sql);
	}
	
	public int save(){
		User user = new User();
		user.setId(1);
		user.setName("dylan");
		//user.setPwd(PasswordUtil.generatePassword("haha"));
		user.setPwd("hahah");
		int count = dao.save(user);
		return count;
	}

	public int update() {
		User user = dao.findById(User.class, 2);
		user.setName("test1");
		user.setPwd(PasswordUtil.generatePassword("1234"));
		user.setSex("1");
		int count  = dao.update(user);
		
		return count;
	}

	public Object delete() {
		User user = new User();
		user.setId(1);
		
		return dao.deleteObject(user);
	}
	
	
}
