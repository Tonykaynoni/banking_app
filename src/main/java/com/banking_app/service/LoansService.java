package com.banking_app.service;



import java.util.List;

import com.banking_app.model.Loans;

public interface LoansService {
	Loans save(Loans loan);
    List<Loans> findAllById(Long userId);
    void delete(long id);
   
}
