/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myapp.spring.projection;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.myapp.spring.domain.entity.TransactionEntity;
import com.myapp.spring.events.TransactionApprovedEvent;
import com.myapp.spring.events.TransactionCreatedEvent;
import com.myapp.spring.events.TransactionRejectedEvent;
import com.myapp.spring.repository.TransactionsRepository;

@Component
@ProcessingGroup("accounts-group")
public class TransactionEventsHandler {
    
    private final TransactionsRepository transactionsRepository;
   
    
    public TransactionEventsHandler(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
        
    }

    @EventHandler
    public void on(TransactionCreatedEvent event ) throws Exception {
        TransactionEntity transactionEntity = new TransactionEntity();

        BeanUtils.copyProperties(event, transactionEntity);


        transactionsRepository.save(transactionEntity);
    }
    
    
    @EventHandler
    public void on(TransactionApprovedEvent transactionApprovedEvent

    ) {
    	TransactionEntity transactionEntity = transactionsRepository.findByTransactionId(transactionApprovedEvent.getTransactionId());
   
    	if(transactionEntity == null) {
    		// TODO: Do something about it
    		return;
    	}
    	
    	transactionEntity.setTransactionStatus(transactionApprovedEvent.getTransactionStatus());

    	
    	
    	transactionsRepository.save(transactionEntity);
    
    }
    
    @EventHandler
    public void on(TransactionRejectedEvent transactionRejectedEvent

    ) {
    	TransactionEntity transactionEntity = transactionsRepository.findByTransactionId(transactionRejectedEvent.getTransactionId());
    	transactionEntity.setTransactionStatus(transactionRejectedEvent.getTransactionStatus());

    	transactionsRepository.save(transactionEntity);
    }
    
}
