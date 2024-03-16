package com.myapp.spring.command;

import java.math.BigDecimal;

public class DebitCommand extends BaseCommand<String> {

	private final BigDecimal debitAmount;
	public DebitCommand(String id,BigDecimal debitAmount) {
		super(id);
		
		this.debitAmount = debitAmount;
		// TODO Auto-generated constructor stub
	}
	
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	
	
	
}
