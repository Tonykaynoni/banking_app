package com.banking_app.service.impl;

import com.banking_app.dao.AccountTypeDoa;

import com.banking_app.model.Account_type;
import com.banking_app.model.User;
import com.banking_app.service.AccountService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service(value = "accountService")
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountTypeDoa acctDao;


	@Override
	public List<Account_type> findAll() {
		List<Account_type> list = new ArrayList<>();
		acctDao.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		acctDao.deleteById(id);	
	}
	
	@Override
    public Account_type save(Account_type acct) {
        return acctDao.save(acct);
    }

	@Override
	public Account_type findbyaccountname(String name) {
        Account_type accountname = acctDao.findByAccountname(name);
		
		return accountname;
	}

	
		
	
}
