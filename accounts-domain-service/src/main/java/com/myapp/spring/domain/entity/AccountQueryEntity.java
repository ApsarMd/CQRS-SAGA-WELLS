package com.myapp.spring.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="accountsquery")
public class AccountQueryEntity {
	
	@Id
	private String id;
	
	private BigDecimal balance;
	
	private Currency currency;
	
	private Status status;
	
	private String owner;
	
	public AccountQueryEntity() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public AccountQueryEntity(String id, BigDecimal balance) {
		super();
		this.id = id;
		this.balance = balance;
	}




	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	@Override
	public String toString() {
		return "AccountQueryEntity [id=" + id + ", balance=" + balance + ", currency=" + currency + ", status=" + status
				+ "]";
	}


	private enum Currency{
		INR,USD
	}
	private enum Status{
		HOLD,COMPLETED
	}
}


