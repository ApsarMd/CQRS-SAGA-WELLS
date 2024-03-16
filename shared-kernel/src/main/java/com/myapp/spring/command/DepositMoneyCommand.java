package com.myapp.spring.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;


@Data
@Builder
public class DepositMoneyCommand {
    @TargetAggregateIdentifier
    private final String accountToId;
    private final String accountFromId;
    private final BigDecimal amount;
    private final String transactionId;

}
