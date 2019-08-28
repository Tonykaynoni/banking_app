package com.banking_app.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.banking_app.dao.UserDao;
import com.banking_app.model.Role;
import com.banking_app.model.TokenInfo;
import com.banking_app.model.User;
import com.banking_app.service.RoleService;
import com.banking_app.service.impl.UserServiceImpl;



@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserDao userD;
    
    @Autowired
    private RoleService rolesService;
    
    
    @Autowired
	private HttpSession session;
    
    
    @RequestMapping(value="/userpage", method = RequestMethod.GET)
    public List<User> listUser(){
        return userService.findAll();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView create(@Valid User user,BindingResult result){
    	if (result.hasErrors()) {
    		 return new ModelAndView("register");
		} 
    	
    	user.setPassword(passwordEncoder.encode(user.getPassword()));
    	Set<Role> role = new HashSet<Role>();
    	role.add(rolesService.getUserRole());
    	user.setRoles(role);
    	userService.save(user);
    	
    	
       // return "success";
        
        return new ModelAndView("redirect:/login?reg=success");
    }
   
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable(value = "id") Long id){
        userService.delete(id);
        return "success";
    }
    
    
    @RequestMapping(value = "/users/credit_account")
    public ModelAndView creditAccount(User userinfo, Model model){
       // userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	 TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = userService.userId();
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("creditAccount");  
    }
    
    @RequestMapping(value = "/users/withdraw")
    public ModelAndView withdraw(User userinfo, Model model){
       // userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	 TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = userService.userId();
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("withdraw");  
    }
    
    @RequestMapping(value = "/users/account_history")
    public ModelAndView trans_histroy(User userinfo, Model model){
       // userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	 TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = userService.userId();
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("transaction_history");  
    }
    
    
    @RequestMapping(value = "/credit_acct", method = RequestMethod.POST)
    public ModelAndView credit(@RequestParam("account_balance") int amount){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
    	user.setAccount_balance(amount + user.getAccount_balance());;
    	user.setId(userid);
    	userD.save(user);
    	//model.addAttribute("info", current_info);
        return new ModelAndView("redirect:/users/credit_account?access_token="+access_token.getAccessToken());  
    }
   
    
    @RequestMapping(value = "/withdraw_acct", method = RequestMethod.POST)
    public ModelAndView withdrawFromAcct(@RequestParam("account_balance") int amount){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
    	user.setAccount_balance(user.getAccount_balance() - amount );;
    	user.setId(userid);
    	userD.save(user);
    	//model.addAttribute("info", current_info);
        return new ModelAndView("redirect:/users/withdraw?access_token="+access_token.getAccessToken());  
    }
    
//    /* It updates record for the given id in editstudent page and redirects to /viewstudents */  
//	 @RequestMapping(value="/editsave",method = RequestMethod.POST)  
//	    public ModelAndView editsave(@ModelAttribute("book") BooksModal emp){  
//	    	System.out.println("id is"+emp.getId());
//	    	bookOp.update(emp);  
//	        return new ModelAndView("redirect:/viewallbooks");  
//	    }  
    
    
   


}
