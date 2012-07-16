package com.dylanvivi.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 常用DateUtil
 * @author dylan
 *
 */
public class DateUtils {
	
	private static Log log = LogFactory.getLog(DateUtils.class);

	public static final String C_DATE_DIVISION = "-";
	
	public static final String C_TIME_PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String C_DATE_PATTERN_DEFAULT = "yyyy-MM-dd";
    public static final String C_DATA_PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String C_TIME_PATTERN_HHMMSS = "HH:mm:ss";
    
    public static final int  C_ONE_SECOND = 1000;
    public static final int  C_ONE_MINUTE = 60 * C_ONE_SECOND;
    public static final int  C_ONE_HOUR   = 60 * C_ONE_MINUTE;
    public static final long C_ONE_DAY    = 24 * C_ONE_HOUR;
    
    /**
     * Return the current date
     * 
     * @return － DATE<br>
     */
    public static Date getCurrentDate(){
    	Calendar cal = Calendar.getInstance();
    	Date currDate = cal.getTime();
    	return currDate;
    }
    
    /**
     * 格式化日期返回string
     * @param date 日期
     * @param pattern 格式
     * @return
     */
    public static String format(Date date,String pattern){
    	DateFormat df = new SimpleDateFormat(pattern);
    	return df.format(date);
    }
    
    /**
     * 以默认方式格式化日期(yyyy-MM-dd HH:mm:ss)
     * @param date
     * @return
     */
    public static String format(Date date){
    	return format(date,C_TIME_PATTERN_DEFAULT);
    }
    
    /**
     * 返回当前时间str (yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static String getCuurentDateStr(){
    	return format(getCurrentDate());
    }
    
    /**
     * 传入时间字符串,加一天后返回Date
     * @param date 时间 格式 YYYY-MM-DD 
     * @return
     */
    public static Date addDate(String date){
        if (date == null){
            return null;
        }

        Date tempDate = parseDate(C_DATE_PATTERN_DEFAULT, date);
        String year = format(tempDate, "yyyy");
        String month = format(tempDate, "MM");
        String day = format(tempDate, "dd");
        
        
        GregorianCalendar calendar = 
            new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
        
        calendar.add(GregorianCalendar.DATE, 1);
        return calendar.getTime();
    }

	/**
	 * string 2 date
	 * @param cDatePattonDefault
	 * @param date
	 * @return
	 */
    public static Date parseDate(String cDatePattonDefault, String date) {
		if(date == null || date.isEmpty()){
			return null;
		}
		if(cDatePattonDefault == null || cDatePattonDefault.isEmpty()){
			cDatePattonDefault = C_DATE_PATTERN_DEFAULT;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(cDatePattonDefault);
		Date newDate = null;
		try {
			newDate = dateFormat.parse(date);
		} catch (ParseException e) {
			log.error("DATE FORMAT ERROR",e);
		}
		return newDate;
	}
    
	/**
	 * string 2 date
	 * @param cDatePattonDefault
	 * @param date
	 * @return
	 */
    public static Date parseDate(String date) {
		String cDatePattonDefault = C_DATE_PATTERN_DEFAULT;
		return parseDate(cDatePattonDefault,date);
	}
}
