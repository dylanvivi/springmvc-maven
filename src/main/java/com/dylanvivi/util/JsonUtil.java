package com.dylanvivi.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
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
public class JsonUtil {
	
	private final static String DATEFORMAT = "yyyy-MM-dd HH:mi:ss";
	
	private static Log log = LogFactory.getLog(JsonUtil.class);
	
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
				return map2JsonObj((Map<String, Object>) obj);
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

	public static JSONObject map2JsonObj(Map<String, Object> o) {
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
	
	 /**
     * 把对象封装为JSON格式
     * 
     * @param o
     *            对象
     * @return JSON格式
     */
    @SuppressWarnings("unchecked")
    public static String toJson(final Object o)
    {
            if (o == null)
            {
                    return "null";
            }
            if (o instanceof String) //String
            {
                    return string2Json((String) o);
            }
            if (o instanceof Boolean) //Boolean
            {
                    return boolean2Json((Boolean) o);
            }
            if (o instanceof Number) //Number
            {
                    return number2Json((Number) o);
            }
            if (o instanceof Map) //Map
            {
                    return map2Json((Map<String, Object>) o);
            }
            if (o instanceof Collection) //List  Set
            {
                    return collection2Json((Collection) o);
            }
            if (o instanceof Object[]) //对象数组
            {
                    return array2Json((Object[]) o);
            }

           if (o instanceof int[])//基本类型数组
            {
                    return intArray2Json((int[]) o);
            }
            if (o instanceof boolean[])//基本类型数组
            {
                    return booleanArray2Json((boolean[]) o);
            }
            if (o instanceof long[])//基本类型数组
            {
                    return longArray2Json((long[]) o);
            }
            if (o instanceof float[])//基本类型数组
            {
                    return floatArray2Json((float[]) o);
            }
            if (o instanceof double[])//基本类型数组
            {
                    return doubleArray2Json((double[]) o);
            }
            if (o instanceof short[])//基本类型数组
            {
                    return shortArray2Json((short[]) o);
            }
            if (o instanceof byte[])//基本类型数组
            {
                    return byteArray2Json((byte[]) o);
            }
            if (o instanceof Object) //保底收尾对象
            {
                    return object2Json(o);
            }

           throw new RuntimeException("不支持的类型: " + o.getClass().getName());
    }

   /**
     * 将 String 对象编码为 JSON格式，只需处理好特殊字符
     * 
     * @param s
     *            String 对象
     * @return JSON格式
     */
    static String string2Json(final String s)
    {
            final StringBuilder sb = new StringBuilder(s.length() + 20);
            sb.append('\"');
            for (int i = 0; i < s.length(); i++)
            {
                    final char c = s.charAt(i);
                    switch (c)
                    {
                    case '\"':
                            sb.append("\\\"");
                            break;
                    case '\\':
                            sb.append("\\\\");
                            break;
                    case '/':
                            sb.append("\\/");
                            break;
                    case '\b':
                            sb.append("\\b");
                            break;
                    case '\f':
                            sb.append("\\f");
                            break;
                    case '\n':
                            sb.append("\\n");
                            break;
                    case '\r':
                            sb.append("\\r");
                            break;
                    case '\t':
                            sb.append("\\t");
                            break;
                    default:
                            sb.append(c);
                    }
            }
            sb.append('\"');
            return sb.toString();
    }

   /**
     * 将 Number 表示为 JSON格式
     * 
     * @param number
     *            Number
     * @return JSON格式
     */
    static String number2Json(final Number number)
    {
            return number.toString();
    }

   /**
     * 将 Boolean 表示为 JSON格式
     * 
     * @param bool
     *            Boolean
     * @return JSON格式
     */
    static String boolean2Json(final Boolean bool)
    {
            return bool.toString();
    }

   /**
     * 将 Collection 编码为 JSON 格式 (List,Set)
     * 
     * @param c
     * @return
     */
    static String collection2Json(final Collection<Object> c)
    {
            final Object[] arrObj = c.toArray();
            return toJson(arrObj);
    }

   /**
     * 将 Map<String, Object> 编码为 JSON 格式
     * 
     * @param map
     * @return
     */
    static String map2Json(final Map<String, Object> map)
    {
            if (map.isEmpty())
            {
                    return "{}";
            }
            final StringBuilder sb = new StringBuilder(map.size() << 4); //4次方
            sb.append('{');
            final Set<String> keys = map.keySet();
            for (final String key : keys)
            {
                    final Object value = map.get(key);
                    sb.append('\"');
                    sb.append(key); //不能包含特殊字符
                    sb.append('\"');
                    sb.append(':');
                    sb.append(toJson(value)); //循环引用的对象会引发无限递归
                    sb.append(',');
            }
            // 将最后的 ',' 变为 '}': 
            sb.setCharAt(sb.length() - 1, '}');
            return sb.toString();
    }

   /**
     * 将数组编码为 JSON 格式
     * 
     * @param array
     *            数组
     * @return JSON 格式
     */
    static String array2Json(final Object[] array)
    {
            if (array.length == 0)
            {
                    return "[]";
            }
            final StringBuilder sb = new StringBuilder(array.length << 4); //4次方
            sb.append('[');
            for (final Object o : array)
            {
                    sb.append(toJson(o));
                    sb.append(',');
            }
            // 将最后添加的 ',' 变为 ']': 
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
    }

   static String intArray2Json(final int[] array)
    {
            if (array.length == 0)
            {
                    return "[]";
            }
            final StringBuilder sb = new StringBuilder(array.length << 4);
            sb.append('[');
            for (final int o : array)
            {
                    sb.append(Integer.toString(o));
                    sb.append(',');
            }
            // set last ',' to ']':
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
    }

   static String longArray2Json(final long[] array)
    {
            if (array.length == 0)
            {
                    return "[]";
            }
            final StringBuilder sb = new StringBuilder(array.length << 4);
            sb.append('[');
            for (final long o : array)
            {
                    sb.append(Long.toString(o));
                    sb.append(',');
            }
            // set last ',' to ']':
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
    }

   static String booleanArray2Json(final boolean[] array)
    {
            if (array.length == 0)
            {
                    return "[]";
            }
            final StringBuilder sb = new StringBuilder(array.length << 4);
            sb.append('[');
            for (final boolean o : array)
            {
                    sb.append(Boolean.toString(o));
                    sb.append(',');
            }
            // set last ',' to ']':
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
    }

   static String floatArray2Json(final float[] array)
    {
            if (array.length == 0)
            {
                    return "[]";
            }
            final StringBuilder sb = new StringBuilder(array.length << 4);
            sb.append('[');
            for (final float o : array)
            {
                    sb.append(Float.toString(o));
                    sb.append(',');
            }
            // set last ',' to ']':
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
    }

   static String doubleArray2Json(final double[] array)
    {
            if (array.length == 0)
            {
                    return "[]";
            }
            final StringBuilder sb = new StringBuilder(array.length << 4);
            sb.append('[');
            for (final double o : array)
            {
                    sb.append(Double.toString(o));
                    sb.append(',');
            }
            // set last ',' to ']':
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
    }

   static String shortArray2Json(final short[] array)
    {
            if (array.length == 0)
            {
                    return "[]";
            }
            final StringBuilder sb = new StringBuilder(array.length << 4);
            sb.append('[');
            for (final short o : array)
            {
                    sb.append(Short.toString(o));
                    sb.append(',');
            }
            // set last ',' to ']':
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
    }

   static String byteArray2Json(final byte[] array)
    {
            if (array.length == 0)
            {
                    return "[]";
            }
            final StringBuilder sb = new StringBuilder(array.length << 4);
            sb.append('[');
            for (final byte o : array)
            {
                    sb.append(Byte.toString(o));
                    sb.append(',');
            }
            // set last ',' to ']':
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
    }

   public static String object2Json(final Object bean)
    {
            //数据检查
            if (bean == null)
            {
                    return "{}";
            }
            final Method[] methods = bean.getClass().getMethods(); //方法数组
            final StringBuilder sb = new StringBuilder(methods.length << 4); //4次方
            sb.append('{');

           for (final Method method : methods)
            {
                    try
                    {
                            final String name = method.getName();
                            String key = "";
                            if (name.startsWith("get"))
                            {
                                    key = name.substring(3);

                                   //防死循环
                                    final String[] arrs =
                                    { "Class" };
                                    boolean bl = false;
                                    for (final String s : arrs)
                                    {
                                            if (s.equals(key))
                                            {
                                                    bl = true;
                                                    continue;
                                            }
                                    }
                                    if (bl)
                                    {
                                            continue; //防死循环
                                    }
                            }
                            else if (name.startsWith("is"))
                            {
                                    key = name.substring(2);
                            }
                            if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0)
                            {
                                    if (key.length() == 1)
                                    {
                                            key = key.toLowerCase();
                                    }
                                    else if (!Character.isUpperCase(key.charAt(1)))
                                    {
                                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                                    }
                                    final Object elementObj = method.invoke(bean);

                                   //System.out.println("###" + key + ":" + elementObj.toString());

                                   sb.append('\"');
                                    sb.append(key); //不能包含特殊字符
                                    sb.append('\"');
                                    sb.append(':');
                                    sb.append(toJson(elementObj)); //循环引用的对象会引发无限递归
                                    sb.append(',');
                            }
                    }
                    catch (final Exception e)
                    {
                            //e.getMessage();
                            throw new RuntimeException("在将bean封装成JSON格式时异常：" + e.getMessage(), e);
                    }
            }
            if (sb.length() == 1)
            {
                    return bean.toString();
            }
            else
            {
                    sb.setCharAt(sb.length() - 1, '}');
                    return sb.toString();
            }
    }

}
