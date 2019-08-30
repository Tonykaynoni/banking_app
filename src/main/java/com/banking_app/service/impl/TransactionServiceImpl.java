package com.banking_app.service.impl;

import com.banking_app.dao.TransactionRepository;
import com.banking_app.model.Transaction;
import com.banking_app.model.User;
import com.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "transactionService")
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
	private TransactionRepository transRepo;

	@Override
	public Transaction save(Transaction trans) {
		return transRepo.save(trans);
	}

	@Override
	public List<Transaction> findHistoryById(Long userId) {
		List<Transaction> list = new ArrayList<>();
		transRepo.findAll().iterator().forEachRemaining(list::add);
		return list;
	}
	



		
	
}
