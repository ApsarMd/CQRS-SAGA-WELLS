package com.myapp.spring.command;

import java.math.BigDecimal;

public class CreditCommand extends BaseCommand<String> {

	private final BigDecimal creditAmount;
	public CreditCommand(String id,
			BigDecimal creditAmount) {
		super(id);
		
		this.creditAmount = creditAmount;
		// TODO Auto-generated constructor stub
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	
	

	
	
	
}
