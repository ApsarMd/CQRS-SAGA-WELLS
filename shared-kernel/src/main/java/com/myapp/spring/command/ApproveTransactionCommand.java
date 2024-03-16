package com.myapp.spring.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApproveTransactionCommand {

	@TargetAggregateIdentifier
	private final String transactionId;
	private final String accountFromId;
	private final String accountToId;
}
