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
	
	 
     private String accountname;
	 
     private Long max_bal;
	 
     private Long min_bal;
	 
     private Long interest_rate;
	 
	 
   
	 public Account_type() {
		super();
	}

	public Account_type(String accountname, Long max_bal, Long min_bal, Long interest_rate) {
		super();
		this.accountname = accountname;
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
 

	public String getAccountname() {
			return accountname;
		}

		public void setAccountname(String accountname) {
			this.accountname = accountname;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account_type other = (Account_type) obj;
		if (id != other.id)
			return false;
		return true;
	}
	   
   
   
	
	
}
