/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myapp.spring.events;

import java.math.BigDecimal;

import com.myapp.spring.model.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreatedEvent {
    private String transactionId;
    private String accountToId;
    private String accountFromId;

    private BigDecimal amount;

   private TransactionStatus transactionStatus;
}
