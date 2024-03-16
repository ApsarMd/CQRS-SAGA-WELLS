package com.myapp.spring.domain.aggregate;

import java.math.BigDecimal;

public class InsufficientBalanceException extends Exception {
	
	private String id;
	private BigDecimal debitAmount;

	public InsufficientBalanceException(String id, BigDecimal debitAmount) {
		this.id = id;
		this.debitAmount = debitAmount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	
	

}
