package com.myapp.spring.events;

import java.math.BigDecimal;

public class AccountCreatedEvent extends BaseEvent<String> {

	private final BigDecimal initialBalance;
	
	private final String owner;

	public AccountCreatedEvent(String id, BigDecimal initialBalance, String owner) {
		super(id);
		this.initialBalance = initialBalance;
		this.owner = owner;
	}

	public BigDecimal getInitialBalance() {
		return initialBalance;
	}

	public String getOwner() {
		return owner;
	}
	
	
	
	
}
