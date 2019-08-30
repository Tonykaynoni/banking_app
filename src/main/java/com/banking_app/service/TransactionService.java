package com.banking_app.service;



import java.util.List;

import com.banking_app.model.Transaction;

public interface TransactionService {

    Transaction save(Transaction trans);
    List<Transaction> findHistoryById(Long userId);
}
