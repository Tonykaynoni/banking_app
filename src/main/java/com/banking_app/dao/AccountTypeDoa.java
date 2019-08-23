package com.banking_app.dao;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.banking_app.model.Account_type;


@Repository
public interface AccountTypeDoa extends CrudRepository<Account_type, Long> {
  //   User findByUsername(String username);
	 
	     
}
