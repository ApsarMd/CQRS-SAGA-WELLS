package com.myapp.spring.model;

import lombok.Value;

@Value
public class TransactionSummary {

	private final String transactionId;
	private final TransactionStatus transactionStatus;
	private final String message;
	
}
