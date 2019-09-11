package com.banking_app.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.banking_app.dao.UserDao;
import com.banking_app.model.Account_type;
import com.banking_app.model.Loans;
import com.banking_app.model.Role;
import com.banking_app.model.TokenInfo;
import com.banking_app.model.Transaction;
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
	    
	    @RequestMapping(value = "/api/credit_acct", method = RequestMethod.POST)
	    public ResponseEntity<String> apicredit(@RequestParam("credit_amount") int amount,Model model,Principal principal){
	    	String username = principal.getName();
	    	User user = userD.findByUsername(username);
	    	Long userid = user.getId();	    	
	    	String acct_type = user.getAccount_type();
	    	Account_type accountDetails = acctImpl.findbyaccountname(acct_type);
	    	if(user.getAccount_balance() == 0 && amount <= accountDetails.getMin_bal() ){
	    		
	    		//user.setId(userid);
	        	//user = userD.findById(userid).get();
	        	//model.addAttribute("info", user);
	        	//model.addAttribute("ses_info", access_token);
	    	  // model.addAttribute("response", "Operation Failed, You must add an amount greater than your minimum allowed amount");
	        	 return new ResponseEntity<>("Minimum_issue",HttpStatus.NOT_ACCEPTABLE);
	    	}
	    	user.setAccount_balance(amount + user.getAccount_balance());
	    	user.setId(userid);
	    	Transaction tr  = new Transaction();
	    	tr.setAmount(amount);
	    	tr.setTransaction_type("Credit");
	    	tr.setUserId(userid);
	    	transLog.save(tr);
	    	userD.save(user);
	    	//model.addAttribute("info", current_info);
	    	 return new ResponseEntity<>("Successful",HttpStatus.OK);
	    }
	    
	    @RequestMapping(value = "/api/withdraw_acct", method = RequestMethod.POST)
	    public ResponseEntity<String> apiwithdrawFromAcct(@RequestParam("amount") int amount,Model model,Principal principal){
	    	String username = principal.getName();
	    	User user = userD.findByUsername(username);
	    	Long userid = user.getId();	    	
	    	
	    	String acct_type = user.getAccount_type();
	    	Account_type accountDetails = acctImpl.findbyaccountname(acct_type);
	        if((user.getAccount_balance() - amount) < accountDetails.getMin_bal()){
	    		
	    		
	    	   // model.addAttribute("response", "Operation Failed, Your Account is too low to complete the transaction");
	    	    return new ResponseEntity<>("Minimum_issue",HttpStatus.NOT_ACCEPTABLE);
	    	}
	    	user.setAccount_balance(user.getAccount_balance() - amount );;
	    	user.setId(userid);
	    	Transaction tr  = new Transaction();
	    	tr.setAmount(amount);
	    	tr.setTransaction_type("Withdraw");
	    	tr.setUserId(userid);
	    	transLog.save(tr);
	    	userD.save(user);
	    	//model.addAttribute("info", current_info);
	    	 return new ResponseEntity<>("Successful",HttpStatus.OK);
	    }
	    
	    @RequestMapping(value = "/api/check_balance")
	    public int apicheckBal(User userinfo, Model model,Principal principal){
	    	String username = principal.getName();
	    	User user = userD.findByUsername(username);
	    	int balance = user.getAccount_balance();	    	
	    	
	    	 return balance;
	    }
	    
	    @RequestMapping(value = "/api/loanuser", method = RequestMethod.POST)
	    public ResponseEntity<String> loanUser(@RequestParam("loan_amt") int amount,Model model,@Valid Loans ln,Principal principal){
	    	String username = principal.getName();
	    	User user = userD.findByUsername(username);
	    	Long userid = user.getId();	   
	    	
	    	user.setAccount_balance(amount + user.getAccount_balance());;
	    	userD.save(user);
	    	
	    	String acct_type = user.getAccount_type();
	    	Account_type accountDetails = acctImpl.findbyaccountname(acct_type);
	    	
	        //Save Loan 
	    	ln.setAmount(amount);
	    	ln.setPayback_amount((double) ((amount/100) * accountDetails.getInterest_rate()) + amount);
	    	ln.setUserid(userid);
	    	ln.setStatus("Not Payed");
	    	loanImpl.save(ln);
	    	
	    	//Save Transaction Log
	    	Transaction tr  = new Transaction();
	    	tr.setAmount(amount);
	    	tr.setTransaction_type("Recieved Loan");
	    	tr.setUserId(userid);
	    	transLog.save(tr);
	    	
	    	//model.addAttribute("info", current_info);
	    	return new ResponseEntity<>("Successful",HttpStatus.OK);
	    }
	    
	    @RequestMapping(value = "/api/buycredit", method = RequestMethod.POST)
	    public ResponseEntity<String> buyCreditFromAcct(@RequestParam("credit_amt") int amount,@RequestParam("phone") Long phone,Model model,Principal principal){
	    	String username = principal.getName();
	    	User user = userD.findByUsername(username);
	    	Long userid = user.getId();	   
	    	String acct_type = user.getAccount_type();
	    	Account_type accountDetails = acctImpl.findbyaccountname(acct_type);
	    	
	    	 if((user.getAccount_balance() - amount) < accountDetails.getMin_bal()){
	     		
	     	
	     	   return new ResponseEntity<>("Minimum_issue",HttpStatus.NOT_ACCEPTABLE);	
	     	}
	    	user.setAccount_balance(user.getAccount_balance() - amount);
	    	user.setId(userid);
	    	userD.save(user);
	        	
	    	//Save Transaction Log
	    	Transaction tr  = new Transaction();
	    	tr.setAmount(amount);
	    	tr.setTransaction_type("Purchased Credit For " + phone);
	    	tr.setUserId(userid);
	    	transLog.save(tr);
	    	
	    	//model.addAttribute("info", current_info);
	    	return new ResponseEntity<>("Successful",HttpStatus.OK);
	    }
	    
	    @RequestMapping(value = "/api/payBills", method = RequestMethod.POST)
	    public ResponseEntity<String> payBillsFromAcct(@RequestParam("credit_amt") int amount,@RequestParam("acct_num") int acct_num,@RequestParam("recipient") String recipient,@RequestParam("bank") String bank,Model model,Principal principal){
	    	String username = principal.getName();
	    	User user = userD.findByUsername(username);
	    	Long userid = user.getId();	   
	    	
	    	String acct_type = user.getAccount_type();
	    	Account_type accountDetails = acctImpl.findbyaccountname(acct_type);
	    	
	    	 if((user.getAccount_balance() - amount) < accountDetails.getMin_bal()){
	     		
//	     		user.setId(userid);
//	         	user = userD.findById(userid).get();
	         	//String profile_pic = "/files/" + user.getProfile_picture();   
	      	 

//	      	    model.addAttribute("pic", profile_pic);
//	         	model.addAttribute("info", user);
//	         	model.addAttribute("ses_info", access_token);
//	     	    model.addAttribute("response", "Operation Failed, Your Account is too low to complete the transaction");
	         	return new ResponseEntity<>("Minimum_issue",HttpStatus.NOT_ACCEPTABLE);		
	     	}
	    	user.setAccount_balance(user.getAccount_balance() - amount);
	    	user.setId(userid);
	    	userD.save(user);
	        	
	    	//Save Transaction Log
	    	Transaction tr  = new Transaction();
	    	tr.setAmount(amount);
	    	tr.setTransaction_type("Made Payment For " + recipient + " Account Number : " + acct_num);
	    	tr.setUserId(userid);
	    	transLog.save(tr);
	    	
	    	//model.addAttribute("info", current_info);
	    	return new ResponseEntity<>("Successful",HttpStatus.OK);
	    }
	    
	    @RequestMapping(value = "/api/account_history")
	    public ResponseEntity<List<Transaction>> trans_histroy(User userinfo, Model model,Principal principal){
	    	String username = principal.getName();
	    	User user = userD.findByUsername(username);
	    	Long userid = user.getId();	     	   
	     	return new ResponseEntity<List<Transaction>>(transLog.findHistoryById(userid),HttpStatus.OK);
	    }
	   

}
