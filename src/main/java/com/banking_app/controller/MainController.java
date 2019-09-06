package com.banking_app.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.banking_app.dao.UserDao;
import com.banking_app.model.Role;
import com.banking_app.model.User;
import com.banking_app.service.RoleService;
import com.banking_app.service.impl.AccountServiceImpl;
import com.banking_app.service.impl.LoansServiceImpl;
import com.banking_app.service.impl.TransactionServiceImpl;
import com.banking_app.service.impl.UserServiceImpl;

@RestController
public class MainController {
	@Autowired
	    private UserServiceImpl userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private TransactionServiceImpl transLog;
    
    @Autowired
    private UserDao userD;
    
    @Autowired
    private RoleService rolesService;
    
    @Autowired 
    private LoansServiceImpl loanImpl;
    
    @Autowired 
    private AccountServiceImpl acctImpl;
    
    @Autowired
	private HttpSession session;
	 @PostMapping("/myApiText")
	    public String listUploadedFiles(Model model) throws IOException {

	       

	        return "success";
	    }
	 
	    @RequestMapping(value = "/apiregister", method = RequestMethod.POST)
	    public ResponseEntity<String> create(@Valid User user,BindingResult result,@RequestParam("username") String myUsername){
	    	if (userService.userExist(myUsername)) {
	    		 return new ResponseEntity<>("UserExist",HttpStatus.CONFLICT);
			} 
	    	user.setPassword(passwordEncoder.encode(user.getPassword()));
	    	Set<Role> role = new HashSet<Role>();
	    	role.add(rolesService.getUserRole());
	    	user.setRoles(role);
	    	userService.save(user);
	    	 return new ResponseEntity<>("Successful",HttpStatus.OK);
	    }

}
