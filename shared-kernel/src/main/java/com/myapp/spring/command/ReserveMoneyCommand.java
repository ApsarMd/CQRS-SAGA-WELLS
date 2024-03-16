package com.myapp.spring.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReserveMoneyCommand {

	@TargetAggregateIdentifier
	private final String accountFromId;
	private final String accountToId;
	private final BigDecimal amount;
	private final String transactionId;

	
	
}
