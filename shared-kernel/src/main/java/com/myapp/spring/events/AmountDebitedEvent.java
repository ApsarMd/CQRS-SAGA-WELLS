package com.myapp.spring.events;

import java.math.BigDecimal;

public class AmountDebitedEvent extends BaseEvent<String> {

	
	private final BigDecimal debitAmount;

	public AmountDebitedEvent(String id, BigDecimal debitAmount) {
		super(id);
		this.debitAmount = debitAmount;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	
	
}
