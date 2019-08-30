package com.banking_app.service;



import java.sql.Date;
import java.util.List;

import com.banking_app.model.Transaction;

public interface TransactionService {

    Transaction save(Transaction trans);
    List<Transaction> findHistoryById(Long userId);
    List<Transaction> searchByInterval(Date startTime, Date endTime, Long userid);

}
