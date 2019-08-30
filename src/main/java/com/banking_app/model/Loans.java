package com.banking_app.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Loans {
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	private Long userid;
	
	private int amount;
	

	private Double payback_amount;
	private String status;
	
	@CreationTimestamp
	private Timestamp duration_fr;
	
	@CreationTimestamp
	private Timestamp duration_tr;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public Timestamp getDuration_fr() {
		return duration_fr;
	}
	public void setDuration_fr(Timestamp duration_fr) {
		this.duration_fr = duration_fr;
	}
	public Timestamp getDuration_tr() {
		return duration_tr;
	}
	public void setDuration_tr(Timestamp duration_tr) {
		this.duration_tr = duration_tr;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Double getPayback_amount() {
		return payback_amount;
	}
	public void setPayback_amount(Double payback_amount) {
		this.payback_amount = payback_amount;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Loans other = (Loans) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
