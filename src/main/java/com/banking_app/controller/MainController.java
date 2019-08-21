package com.banking_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping(value="/login")
	public String homePage() {
 		return "login.html";
 	} 
	
	@RequestMapping(value="/register")
	public String regPage() {
 		return "register.html";
 	} 


}
