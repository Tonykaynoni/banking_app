package com.banking_app.dao;


import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.banking_app.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

	List<Transaction> findAllByUserId(Long userid);
	//Transaction findAllById(Long userid);
	List<Transaction> findAllByTransTimeBetweenAndUserId(Date startDate, Date endTime, Long userid);

}
