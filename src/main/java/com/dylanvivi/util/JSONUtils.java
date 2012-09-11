package com.dylanvivi.util;

import java.sql.Date;

import net.sf.json.JsonConfig;

public class JSONUtils {
	
	public static JsonConfig  getJsonConfig(){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());  
		return jsonConfig;
	}
}
