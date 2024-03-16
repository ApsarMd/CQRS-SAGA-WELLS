package com.myapp.spring.command;

import java.math.BigDecimal;

public class NewAccountCreateCommand extends BaseCommand<String> {

	private final BigDecimal accountBalance;
	
	private final String accountHolder; 
	public NewAccountCreateCommand(String id,
			BigDecimal accountBalance,String accountHolder) {
		super(id);
		
		this.accountBalance = accountBalance;
		this.accountHolder = accountHolder;
		// TODO Auto-generated constructor stub
	}
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}
	public String getAccountHolder() {
		return accountHolder;
	}
	
	
	
	

	
	
	
}
