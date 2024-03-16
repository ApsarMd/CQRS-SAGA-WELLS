package com.myapp.spring.events;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MoneyDepositedEvent {
    private final String accountFromId;
    private final String accountToId;
    private final BigDecimal amount;
    private final String transactionId;


}
