package com.myapp.spring.dto;

import java.math.BigDecimal;

public class AccountCreateDTO {
	
	private final String id;
	private final BigDecimal initialBalance;
	private final String owner;
	public AccountCreateDTO(String id,BigDecimal initialBalance, String owner) {
		super();
		this.id = id;
		this.initialBalance = initialBalance;
		this.owner = owner;
	}
	public BigDecimal getInitialBalance() {
		return initialBalance;
	}
	public String getOwner() {
		return owner;
	}
	
	public String getId() {
		return id;
	}
	
	
	

}
