package com.dylanvivi.pojo;


import java.sql.Date;

import com.dylanvivi.dao.annotation.Column;
import com.dylanvivi.dao.annotation.Id;
import com.dylanvivi.dao.annotation.Table;


/**
 * 通过数据库内表的字段动态生成 javabean
 * @author dylan
 **/
@Table(name = "USER")
public class User {	
	@Id
	@Column(name="ID")
	private int id;

	@Column(name="NAME")
	private String name;

	@Column(name="SEX")
	private String sex;

	@Column(name="PWD")
	private String pwd;


	public User() {

	}

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSex() {
		return this.sex;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPwd() {
		return this.pwd;
	}
}
