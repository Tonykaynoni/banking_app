package com.banking_app.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking_app.dao.LoansRepository;
import com.banking_app.model.Loans;
import com.banking_app.service.LoansService;
@Service(value = "loanService")
public class LoansServiceImpl implements LoansService{
    
	
	@Autowired 
	private LoansRepository loanrepo;
	
	@Override
	public Loans save(Loans loan) {
		return loanrepo.save(loan);
	}

	@Override
	public List<Loans> findAllById(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		loanrepo.deleteById(id);
	}


}
