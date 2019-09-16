package com.banking_app.controller;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.banking_app.storage.StorageService;



@Controller
public class UserController {
	 private final StorageService storageService;
	 @Autowired
	    public UserController(StorageService storageService) {
	        this.storageService = storageService;
	    }
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
	private TokenStore tokenStore;
    
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
        
        return new ModelAndView("redirect:/?reg=success");
    }
   
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable(value = "id") Long id){
        userService.delete(id);
        return "success";
    }
    
    
  	@RequestMapping(value="/")
  	public String userLoginPage() {
   		return "login";
   	} 
  	
  	@RequestMapping(value="/register")
  	public String regPage(User user, Model model) {
  		   List<Account_type> list = acctImpl.findAll();
  		    model.addAttribute("accounts", list);
   		return "register";
   	}
  	
  	
  	 
      @RequestMapping(value = "/process_login/{access_token}/{refresh_token}")
      public String register(@PathVariable Map<String,String> pathValues) throws Exception{
          String access_token = pathValues.get("access_token");
          String refresh_token = pathValues.get("refresh_token");
          
          TokenInfo info = new TokenInfo(access_token,refresh_token);
          session.setAttribute("session_access_details", info);

          return ("redirect:/users/home?access_token="+info.getAccessToken());
      }
      
    
    @RequestMapping(value = "/users/credit_account")
    public ModelAndView creditAccount(User userinfo, Model model){
        //userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");  
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	String profile_pic = "/files/" + current_info.getProfile_picture();   
  	    model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
  	    model.addAttribute("pic", profile_pic);
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
    	String profile_pic = "/files/" + current_info.getProfile_picture();   
  	    model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

  	    model.addAttribute("pic", profile_pic);
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
    	String profile_pic = "/files/" + userinfo.getProfile_picture();   
  	    model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

  	    model.addAttribute("pic", profile_pic);
    	model.addAttribute("info", userinfo);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("home");  
      }
    
    @RequestMapping(value = "/users/withdraw")
    public ModelAndView withdraw(User userinfo, Model model){
        //userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	String profile_pic = "/files/" + current_info.getProfile_picture();   
  	    model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

  	    model.addAttribute("pic", profile_pic);
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("withdraw");  
    }
    
    @RequestMapping(value = "/users/account_history")
    public ModelAndView trans_histroy(User userinfo, Model model){
       
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	String profile_pic = "/files/" + current_info.getProfile_picture();   
  	    model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

  	    model.addAttribute("pic", profile_pic);
    	model.addAttribute("info", current_info);
    	model.addAttribute("list", transLog.findHistoryById(userid));
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("transaction_history");  
    }
    
    @RequestMapping(value = "/users/profile")
    public ModelAndView profile(User userinfo, Model model){
       // userService.delete(id);
    	//String currentUsername = principal.getName();
    	//userService.loadUserByUsername(currentUsername);
    	 TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	String profile_pic = "/files/" + current_info.getProfile_picture();   
    	  model.addAttribute("files", storageService.loadAll().map(
                  path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                          "serveFile", path.getFileName().toString()).build().toString())
                  .collect(Collectors.toList()));

    	model.addAttribute("info", current_info);
    	model.addAttribute("pic", profile_pic);
    	model.addAttribute("list", transLog.findHistoryById(userid));
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("profile");  
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
    public ModelAndView credit(@RequestParam("credit_amount") int amount,Model model){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
    	String acct_type = user.getAccount_type();
    	Account_type accountDetails = acctImpl.findbyaccountname(acct_type);
    	if(user.getAccount_balance() == 0 && amount <= accountDetails.getMin_bal() ){
    		
    		user.setId(userid);
        	user = userD.findById(userid).get();
        	model.addAttribute("info", user);
        	model.addAttribute("ses_info", access_token);
    	   model.addAttribute("response", "Operation Failed, You must add an amount greater than your minimum allowed amount");
    	   return new ModelAndView("creditAccount");   	
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
        return new ModelAndView("redirect:/users/take_loan?access_token="+access_token.getAccessToken());  
    }
    
    @RequestMapping(value = "/users/pay_bills")
    public ModelAndView payBills(User userinfo, Model model){
    	
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	userinfo.setId(userid);
    	User current_info = userD.findById(userid).get();
    	String profile_pic = "/files/" + current_info.getProfile_picture();   
  	    model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

  	    model.addAttribute("pic", profile_pic);
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
    	String profile_pic = "/files/" + current_info.getProfile_picture();   
  	    model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

  	    model.addAttribute("pic", profile_pic);
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("checkBalance");  
    }
    
    @RequestMapping(value = "/payBills", method = RequestMethod.POST)
    public ModelAndView payBillsFromAcct(@RequestParam("credit_amt") int amount,@RequestParam("acct_num") int acct_num,@RequestParam("recipient") String recipient,@RequestParam("bank") String bank,Model model){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
    	String acct_type = user.getAccount_type();
    	Account_type accountDetails = acctImpl.findbyaccountname(acct_type);
    	
    	 if((user.getAccount_balance() - amount) < accountDetails.getMin_bal()){
     		
     		user.setId(userid);
         	user = userD.findById(userid).get();
         	String profile_pic = "/files/" + user.getProfile_picture();   
      	    model.addAttribute("files", storageService.loadAll().map(
                    path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "serveFile", path.getFileName().toString()).build().toString())
                    .collect(Collectors.toList()));

      	    model.addAttribute("pic", profile_pic);
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
    	String profile_pic = "/files/" + current_info.getProfile_picture();   
  	    model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

  	    model.addAttribute("pic", profile_pic);
    	model.addAttribute("info", current_info);
    	model.addAttribute("ses_info", access_token);
    	return new ModelAndView("buycredit");  
    }
    
    @RequestMapping(value = "/buycredit", method = RequestMethod.POST)
    public ModelAndView buyCreditFromAcct(@RequestParam("credit_amt") int amount,@RequestParam("phone") Long phone,Model model,@Valid Loans ln){
    	TokenInfo access_token = (TokenInfo) session.getAttribute("session_access_details");
    	   
    	Long userid = (Long) session.getAttribute("session_user_id");
    	User user = userD.findById(userid).get();
    	String acct_type = user.getAccount_type();
    	Account_type accountDetails = acctImpl.findbyaccountname(acct_type);
    	
    	 if((user.getAccount_balance() - amount) < accountDetails.getMin_bal()){
     		
     		user.setId(userid);
         	user = userD.findById(userid).get();
         	model.addAttribute("info", user);
         	model.addAttribute("ses_info", access_token);
     	    model.addAttribute("response", "Operation Failed, Your Account is too low to complete the transaction");
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
    	String acct_type = user.getAccount_type();
    	Account_type accountDetails = acctImpl.findbyaccountname(acct_type);
        if((user.getAccount_balance() - amount) < accountDetails.getMin_bal()){
    		
    		user.setId(userid);
        	user = userD.findById(userid).get();
        	model.addAttribute("info", user);
        	model.addAttribute("ses_info", access_token);
    	    model.addAttribute("response", "Operation Failed, Your Account is too low to complete the transaction");
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
    
    @GetMapping("/log-out")
  		public String logOutUser(@RequestParam("access_token") String token) {
  			
  			try{
  				OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
  				if (oAuth2AccessToken != null) {
  					OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
  					tokenStore.removeAccessToken(oAuth2AccessToken);
  					if (oAuth2RefreshToken != null) {
  						tokenStore.removeRefreshToken(oAuth2RefreshToken);
  						 return "login";  
  					}
  				}

  			} catch (Exception e) {
  				System.out.println("Error while logging out because: " + e.getMessage());
  				 return "Invalid Access Token";  
  				//return new ResponseEntity<>("Invalid Access Token",HttpStatus.OK);
  			}
  			 return "Logout Failed";  
  			     //return new ResponseEntity<>("Logout Failed",HttpStatus.OK);

  		}
    

    
    
   


}
