package com.myapp.spring.events;

import com.myapp.spring.model.TransactionStatus;

import lombok.Value;

@Value
public class TransactionRejectedEvent {
	private final String transactionId;
	private final String reason;
	private final TransactionStatus transactionStatus = TransactionStatus.REJECTED;
}
