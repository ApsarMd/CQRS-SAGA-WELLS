package com.myapp.spring.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MoneyRollbackCommand {

	@TargetAggregateIdentifier
	private final String accountId;
	
	private final BigDecimal amount;
	private final String transactionId;

	private final String reason;
	
}
