package com.myapp.spring.events;

import java.math.BigDecimal;

public class AmountCreditedEvent extends BaseEvent<String> {

	
	private final BigDecimal creditAmount;

	public AmountCreditedEvent(String id, BigDecimal creditAmount) {
		super(id);
		this.creditAmount = creditAmount;
	}
	
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	
}
