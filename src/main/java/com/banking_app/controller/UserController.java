package com.banking_app.controller;

import java.sql.Date;
import java.sql.Timestamp;
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

import com.banking_app.dao.TransactionRepository;
import com.banking_app.dao.UserDao;
import com.banking_app.model.Loans;
import com.banking_app.model.Role;
import com.banking_app.model.TokenInfo;
import com.banking_app.model.Transaction;
import com.banking_app.model.User;
import com.banking_app.service.RoleService;
import com.banking_app.service.impl.LoansServiceImpl;
import com.banking_app.service.impl.TransactionServiceImpl;
import com.banking_app.service.impl.UserServiceImpl;



@RestController
public class UserController {

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
    	   
    	 Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("creditAccount");  
    }
    
    @RequestMapping(value = "/users/take_loan")
    public ModelAndView takeLoan(User userinfo, Model model){
       // userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	 TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	 Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("takeloan");  
    }
    
    
    
    @RequestMapping(value = "/users/home")
    public ModelAndView home(User userinfo, Model model){
       // userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	 TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	 Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	userinfo = userD.findById(userid).get();
    	model.addAttribute("info", userinfo);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("home");  
      }
    
    @RequestMapping(value = "/users/withdraw")
    public ModelAndView withdraw(User userinfo, Model model){
       // userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	 TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
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
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("list", transLog.findHistoryById(userid));
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("transaction_history");  
    }
    @RequestMapping(value = "/search_bydate")
    public ModelAndView trans_histroy_search(User userinfo, Model model,@RequestParam("fromdate") Date fromdate,@RequestParam("todate") Date todate){
       // userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	 TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("list", transLog.searchByInterval(fromdate, todate, userid));
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("transaction_history");  
    }
    
    
    @RequestMapping(value = "/credit_acct", method = RequestMethod.POST)
    public ModelAndView credit(@RequestParam("account_balance") int amount,Model model){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
    	if(user.getAccount_balance() == 0 && amount <= 300 ){
    		
    		user.setId(userid);
        	user = userD.findById(userid).get();
        	model.addAttribute("info", user);
        	model.addAttribute("ses_info", access_token);
    	   model.addAttribute("response", "Operation Failed, You must add an amount less than your minimum allowed amount");
    	   return new ModelAndView("creditAccount");   	
    	}
    	user.setAccount_balance(amount + user.getAccount_balance());;
    	user.setId(userid);
    	Transaction tr  = new Transaction();
    	tr.setAmount(amount);
    	tr.setTransaction_type("Credit");
    	tr.setUserId(userid);
    	transLog.save(tr);
    	userD.save(user);
    	//model.addAttribute("info", current_info);
        return new ModelAndView("redirect:/users/credit_account?access_token="+access_token.getAccessToken());  
    }
    
    @RequestMapping(value = "/loanuser", method = RequestMethod.POST)
    public ModelAndView loanUser(@RequestParam("loan_amt") int amount,Model model,@Valid Loans ln){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
    	
    	user.setAccount_balance(amount + user.getAccount_balance());;
    	user.setId(userid);
    	userD.save(user);
    	
    	
    	
        //Save Loan 
    	ln.setAmount(amount);
    	ln.setPayback_amount((double) ((amount/100) * 10) + amount);
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
        return new ModelAndView("redirect:/users/take_loan?access_token="+access_token.getAccessToken());  
    }
    
    @RequestMapping(value = "/users/pay_bills")
    public ModelAndView payBills(User userinfo, Model model){
        //userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("paybills");  
    }
    
    @RequestMapping(value = "/users/check_balance")
    public ModelAndView checkBal(User userinfo, Model model){
        //userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("checkBalance");  
    }
    
    @RequestMapping(value = "/payBills", method = RequestMethod.POST)
    public ModelAndView payBillsFromAcct(@RequestParam("credit_amt") int amount,@RequestParam("acct_num") int acct_num,@RequestParam("recipient") String recipient,@RequestParam("bank") String bank,Model model){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
    	 if((user.getAccount_balance() - amount) < 300){
     		
     		user.setId(userid);
         	user = userD.findById(userid).get();
         	model.addAttribute("info", user);
         	model.addAttribute("ses_info", access_token);
     	    model.addAttribute("response", "Operation Failed, Your Account is too low to complete the transaction");
     	   return new ModelAndView("paybills");   	
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
        return new ModelAndView("redirect:/users/pay_bills?access_token="+access_token.getAccessToken());  
    }
    @RequestMapping(value = "/users/buy_credit")
    public ModelAndView buy_credit(User userinfo, Model model){
        //userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("buycredit");  
    }
    
    @RequestMapping(value = "/buycredit", method = RequestMethod.POST)
    public ModelAndView buyCreditFromAcct(@RequestParam("credit_amt") int amount,@RequestParam("phone") Long phone,Model model,@Valid Loans ln){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
    	 if((user.getAccount_balance() - amount) < 300){
     		
     		user.setId(userid);
         	user = userD.findById(userid).get();
         	model.addAttribute("info", user);
         	model.addAttribute("ses_info", access_token);
     	    model.addAttribute("response", "Operation Failed, You can't buy credit more than your minimum balance");
     	   return new ModelAndView("buycredit");   	
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
        return new ModelAndView("redirect:/users/buy_credit?access_token="+access_token.getAccessToken());  
    }
   
   
    
    @RequestMapping(value = "/withdraw_acct", method = RequestMethod.POST)
    public ModelAndView withdrawFromAcct(@RequestParam("account_balance") int amount,Model model){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
        if((user.getAccount_balance() - amount) < 300){
    		
    		user.setId(userid);
        	user = userD.findById(userid).get();
        	model.addAttribute("info", user);
        	model.addAttribute("ses_info", access_token);
    	    model.addAttribute("response", "Operation Failed, You can't withdraw more than your minimum balance");
    	   return new ModelAndView("withdraw");   	
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
