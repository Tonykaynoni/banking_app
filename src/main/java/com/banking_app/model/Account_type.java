package com.banking_app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Account_type {
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
	
	 @Column
     private String account_name;
	 @Column
     private Long max_bal;
	 @Column
     private Long min_bal;
	 @Column
     private Long interest_rate;
	 
	 
   
	 public Account_type(String account_name, Long max_bal, Long min_bal, Long interest_rate) {
		super();
		this.account_name = account_name;
		this.max_bal = max_bal;
		this.min_bal = min_bal;
		this.interest_rate = interest_rate;
	}

	public long getId() {
	        return id;
	    }

	    public void setId(long id) {
	        this.id = id;
	    }
 
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public Long getMax_bal() {
		return max_bal;
	}
	public void setMax_bal(Long max_bal) {
		this.max_bal = max_bal;
	}
	public Long getMin_bal() {
		return min_bal;
	}
	public void setMin_bal(Long min_bal) {
		this.min_bal = min_bal;
	}
	public Long getInterest_rate() {
		return interest_rate;
	}
	public void setInterest_rate(Long interest_rate) {
		this.interest_rate = interest_rate;
	}
	   
   
   
	
	
}
