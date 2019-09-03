package com.banking_app.service;



import java.util.List;

import com.banking_app.model.Account_type;

public interface AccountService {

	Account_type save(Account_type acct);
    List<Account_type> findAll();
    void delete(long id);
    Account_type findbyaccountname(String name);
}
