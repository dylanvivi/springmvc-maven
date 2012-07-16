package com.dylanvivi.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 学习Object to Json
 * @author dylan
 * @since 2012.07
 *
 */
public class JsonUtils {
	
	private final static String DATEFORMAT = "yyyy-MM-dd HH:mi:ss";
	
	private static Log log = LogFactory.getLog(JsonUtils.class);
	
	/**
	 * 日期型转换格式
	 * @param date
	 * @return
	 */
	static String dateFormat(Date date){
		DateFormat df = new SimpleDateFormat(DATEFORMAT);
		String time = df.format(date);
		return time;
	}
	
	/**
	 * @category Object to JsonObj
	 * @return jsonobj
	 */
	public static JSONObject toJsonOBJ(Object obj){
		try{
			if(obj == null || isNormalType(obj)){
				return null;
			}
			//map
			if(obj instanceof Map){
				return map2Json((Map<String, Object>) obj);
			}
			//list封装一个result-JsonArray的数组
			if(obj instanceof List){
				JSONObject json = new JSONObject();
				json.put("result", list2Json((List)obj));
				return json;
				
			}
			if(obj instanceof Object){
				return Obj2Json(obj);
			}
		}catch(Exception e){
			log.error("Can't cast this type! ",e);
		}
		return null;
	}
	
	
	public static JSONArray list2Json(List list) {
		JSONArray arr = new JSONArray();
		if(list == null){
			return arr;
		}
		for(Object obj : list){
			JSONObject jo = new JSONObject();
			jo = toJsonOBJ(obj);
			arr.put(jo);
		}
		return arr;
	}

	public static JSONObject map2Json(Map<String, Object> o) {
		JSONObject obj = new JSONObject();
		Set<String> keys = o.keySet();
		try{
			for(String key : keys){
				Object values = o.get(key);
				if(!isNormalType(values)){ //非基本类型
					JSONObject child = toJsonOBJ(values); // 迭代value
					obj.put(key, child);
				}else{
					obj.put(key, values);
				}
			}
		}catch(Exception e){
			log.error("Can't cast this type! ",e);
		}
		return obj;
	}

	/**
	 * 简单的obj2json
	 * 未解决：obj里面有list时返回{result:[jsonarray]}...
	 * @param obj
	 * @return
	 */
	public static JSONObject Obj2Json(Object obj){
		JSONObject data = new JSONObject();
		try{
			//如果obj是空
			if(obj == null){
				return null;
			}
			//得到类的所有方法(实例类)
			Method[] methods = obj.getClass().getMethods();
			for(Method method : methods){
				String name = method.getName(); //得到方法名
				String key = "";  //key
				if(name.startsWith("get")){ //getter方法;
					key = name.substring(3).toLowerCase(); //小写
					//防死循环(自带方法中对应的getClass()方法)
					final String[] arrs = { "Class" };
					boolean bl = false;
					for (final String s : arrs){
						if (s.equalsIgnoreCase(key)){
							bl = true;
							continue;
						}
					}
					if (bl){
						//防死循环
						continue; 
					}
				}else if(name.startsWith("is")){ //boolean 类型
					key = name.substring(2).toLowerCase();
				}
				
				
				if(key.length() > 0){
					//这个方法里对应的值    可能是一个obj(class)
					Object o = method.invoke(obj);
					if(o == null){ //返回值为空  void
						continue;
					}
					
					
					//如果返回的是一个基本类型, 则当做value put进jsonobj
					if(isNormalType(o)){
						data.put(key, o);
					}else{
					//如果返回一个obj则迭代此class，返回一个JsonObj放进此key里
						JSONObject child = toJsonOBJ(o);
						data.put(key, child);
					}
					
					
				}
				
				
			}
		}catch(Exception e){
			log.error("Can't cast this type! ",e);
		}
		//System.out.println("@@@@@dylan test@@@@@"+data.toString());
		return data;
	}
	
	private static boolean isNormalType(Object o){
		//如果是基本类型
		if(o instanceof String || o instanceof Integer || o instanceof Float || o instanceof Boolean  || o instanceof Short ||  o instanceof Double || 
		         o instanceof Long || o instanceof BigDecimal ||  o instanceof BigInteger ||   o instanceof Byte || o instanceof Date){
			return true;
			
		}
		return false;
	}
}
