package com.banking_app.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.banking_app.model.User;
import com.banking_app.service.UserService;



@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
   @Autowired
   private HttpSession session;
    
    @RequestMapping(value="/user", method = RequestMethod.GET)
    public List<User> listUser(){
        return userService.findAll();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView create(@Valid User user,BindingResult result, ModelMap model,RedirectAttributes redirectAttributes){
    	if (result.hasErrors()) {
    		 return new ModelAndView("redirect:/register?reg=failed");
		} 
    	
    	user.setPassword(passwordEncoder.encode(user.getPassword()));
    	userService.save(user);
       // return "success";
        
        return new ModelAndView("redirect:/login?reg=success");
    }
   
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable(value = "id") Long id){
        userService.delete(id);
        return "success";
    }
    
    
    @RequestMapping(value = "/process_login/{access_token}/{refresh_token}", method = RequestMethod.GET)
    public ModelAndView register(@PathVariable Map<String,String> pathValues){
        String access_token = pathValues.get("access_token");
        String refresh_token = pathValues.get("refresh_token");
        
        
        //System.out.println(access_token);
        //System.out.println(refresh_token);
    	//userService.delete(id);
        session.setAttribute("session_access_tok", access_token);
        session.setAttribute("session_refresh_tok", refresh_token);
        
        
        return new ModelAndView("redirect:/user?access_token="+access_token);
    }
    
    
   


}
