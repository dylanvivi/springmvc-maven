package com.dylanvivi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dylanvivi.dao.IBaseDao;

@Service
public class TestService {
	
	@Autowired
	private IBaseDao dao;
	
	public boolean check(){
		String sql = "SELECT * FROM sys_user";
		return dao.exists(sql);
	}
	
	
	
}
