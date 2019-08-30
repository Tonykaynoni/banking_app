package com.banking_app.dao;


import org.springframework.data.repository.CrudRepository;

import com.banking_app.model.Loans;

public interface LoansRepository extends CrudRepository<Loans, Long> {

	Loans findAllById(Long userid);
	



}
