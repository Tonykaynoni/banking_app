package com.banking_app.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.banking_app.model.Account_type;
import com.banking_app.model.TokenInfo;
import com.banking_app.model.User;
import com.banking_app.service.AccountService;

@Controller
public class MainController {
	
	@Autowired 
	private AccountService acct;
	
	@Autowired
	private HttpSession session;
	  
	@RequestMapping(value="/login")
	public String userLoginPage() {
 		return "login";
 	} 
	
	@RequestMapping(value="/register")
	public String regPage(User user, Model model) {
		   List<Account_type> list = acct.findAll();
		    model.addAttribute("accounts", list);
 		return "register.html";
 	}
	
	@RequestMapping(value="/")
	public String homePage() {		 
 		return "index";
 	}
	 
    @RequestMapping(value = "/process_login/{access_token}/{refresh_token}")
    public String register(@PathVariable Map<String,String> pathValues) throws Exception{
        String access_token = pathValues.get("access_token");
        String refresh_token = pathValues.get("refresh_token");
        
        TokenInfo info = new TokenInfo(access_token,refresh_token);
        //System.out.println(access_token);
        //System.out.println(refresh_token);
    	//userService.delete(id);
        session.setAttribute("session_access_details", info);
        
        String a =(String) session.getAttribute("session_access_tok");
        
        
        return ("redirect:/userpage?access_token="+info.getAccessToken());
    }


}
