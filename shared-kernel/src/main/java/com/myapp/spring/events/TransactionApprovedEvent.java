package com.myapp.spring.events;

import com.myapp.spring.model.TransactionStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionApprovedEvent {

	private final String transactionId;
	private final TransactionStatus transactionStatus = TransactionStatus.APPROVED;
	private final String accountToId;
	private final String accountFromId;
	
}
