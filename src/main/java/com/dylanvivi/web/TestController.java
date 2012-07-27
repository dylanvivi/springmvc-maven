package com.dylanvivi.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dylanvivi.service.TestService;

@Controller
public class TestController {
	
	private Log log = LogFactory.getLog(TestController.class);
	
	@Autowired
	private TestService service;
	
	@RequestMapping("index")
	public String test(Model model){
		model.addAttribute("hello", "hello World!~");
		System.out.println("test service:"+ service.check());
		return "index";
	}
	
	@RequestMapping("testsave")
	public String save(Model model){
		model.addAttribute("hello", "test Save~");
		log.debug("save "+service.save());
		return "index";
	}
	
	@RequestMapping("testupdate")
	public ModelAndView update(Model model){
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("hello",service.update());
		return mv;
	}
	
	@RequestMapping("testdel")
	public ModelAndView delete(Model model){
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("hello",service.delete());
		return mv;
	}
}
