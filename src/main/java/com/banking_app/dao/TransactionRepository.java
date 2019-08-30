package com.banking_app.dao;


import org.springframework.data.repository.CrudRepository;

import com.banking_app.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

	Transaction findAllById(Long userid);
	//Transaction findAllById(Long userid);



}
