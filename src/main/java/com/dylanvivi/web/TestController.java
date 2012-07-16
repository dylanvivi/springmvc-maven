package com.dylanvivi.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dylanvivi.service.TestService;

@Controller
public class TestController {
	
	@Autowired
	private TestService service;
	
	@RequestMapping("index")
	public String test(Model model){
		model.addAttribute("hello", "hello World!~");
		System.out.println("test service:"+ service.check());
		return "index";
	}
}
