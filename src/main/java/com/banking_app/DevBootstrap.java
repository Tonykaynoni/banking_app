package com.banking_app;

//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//import com.banking_app.dao.AccountTypeDoa;
//import com.banking_app.model.Account_type;
//
//@Component
//public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent>{
//	
//	private AccountTypeDoa account_typeDoa;
//	
//	public DevBootstrap(AccountTypeDoa account_typeDoa) {
//		this.account_typeDoa = account_typeDoa;
//	}
//	@Override
//	public void onApplicationEvent(ContextRefreshedEvent event) {
//		initData();
//	}
//	
//	public void initData() {
//		Account_type x = new Account_type("Savings",  (long) 200, (long) 200000,  (long)10);
//		Account_type x1 = new Account_type("Current",  (long) 1000, (long) 1000000000,  (long)20);
//		Account_type x2 = new Account_type("Student Savings",  (long) 200, (long) 100000,  (long)15);
//		account_typeDoa.save(x);
//		account_typeDoa.save(x1);
//		account_typeDoa.save(x2);
//
//}
//	
//}
