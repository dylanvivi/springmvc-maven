package com.dylanvivi.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.dylanvivi.dao.IBaseDao;

@Controller
public class BaseController extends MultiActionController{
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	public static final String MESSAGES_KEY = "successMessages";
	public static final String ERRORS_KEY = "errorMessages";
	
	@Autowired
	private IBaseDao dao;
	
	public void saveMessage(HttpServletRequest request,String msg){
		HttpSession session = request.getSession();
		List<String> message = (List<String>)session.getAttribute(MESSAGES_KEY);
		if(message == null){
			message = new ArrayList<String>();
		}
		message.add(msg);
		session.setAttribute(MESSAGES_KEY, message);
		
	}
	
	
	public void saveErrors(HttpServletRequest request,String error){
		HttpSession session = request.getSession();
		List<String> errors = (List<String>)session.getAttribute(ERRORS_KEY);
		if(errors == null){
			errors = new ArrayList<String>();
		}
		errors.add(error);
		session.setAttribute(ERRORS_KEY, errors);
	}
	
	public String getText(String msgKey){
		return getMessageSourceAccessor().getMessage(msgKey);
	}
	
	public String getText(String msgKey, String arg) {
		return getText(msgKey, new Object[] { arg });
	}

	public String getText(String msgKey, Object[] args) {
		return getMessageSourceAccessor().getMessage(msgKey, args);
	}
	
	public HttpSession getSession(HttpServletRequest request){
		return request.getSession();
	}
	
	public Object[] getArgs(List<Object> args) {
		if (args == null || args.isEmpty())
			return null;
		int len = args.size();
		Object[] objArgs = new Object[len];
		for (int i = 0; i < len; i++) {
			objArgs[i] = args.get(i);
		}
		return objArgs;
	}
	
	public void putSession(HttpServletRequest request,String attributeKey,Object value){
		getSession(request).setAttribute(attributeKey, value);
	}
	
	public Object getSession(HttpServletRequest request,String attributeKey){
		return getSession(request).getAttribute(attributeKey);
	}
	
	/**
	 * 直接输出纯字符串
	 */
	public void renderText(HttpServletResponse response, String content) {
		try {
			//response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	/**
	 * 直接输出纯HTML
	 */
	public void renderHtml(HttpServletResponse response, String content) {
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(content);
		} catch (IOException e) {
			log.error("", e);
		}
	}

	
	/**
	 * 直接输出纯XML
	 */
	public void renderXML(HttpServletResponse response, String content) {
		try {
			response.setContentType("text/xml;charset=UTF-8");
			response.getWriter().write(content);
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	/**
	 * 把JSON数据以字符串形式输出
	 */
	protected void renderTextHTML(JSONObject data, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		log.info(data.toString());
		try {
			if (data != null)
				response.getWriter().print(data.toString());
		} catch (IOException e) {
			log.error("ERROR", e.getMessage());
		}
	}

	/**
	 * 把JSON数据以字符串形式输出
	 */
	protected void renderTextHTML(String data, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		log.info(data.toString());
		try {
			if (data != null)
				response.getWriter().print(data.toString());
		} catch (IOException e) {
			log.error("ERROR", e.getMessage());
		}
	}

	/**
	 * 直接输出json
	 */
	public void renderJson(HttpServletResponse response, String content) {
		try {
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().write(content);
		} catch (IOException e) {
			log.error("", e);
		}
	}
}
