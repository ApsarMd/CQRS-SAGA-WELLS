/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myapp.spring.command;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.myapp.spring.model.TransactionStatus;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateTransactionCommand {
        
    @TargetAggregateIdentifier
    public final String transactionId;

    private final String accountToId;
    private final String accountFromId;
    private final BigDecimal amount;

    private final TransactionStatus transactionStatus;
}
