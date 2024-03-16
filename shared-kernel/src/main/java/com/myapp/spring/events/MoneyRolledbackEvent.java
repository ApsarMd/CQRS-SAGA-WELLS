package com.myapp.spring.events;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MoneyRolledbackEvent {

	private final String accountId;
	private final BigDecimal amount;
	private final String transactionId;

	private final String reason;
	
}
