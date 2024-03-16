
package com.myapp.spring.aggregate;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.myapp.spring.command.ApproveTransactionCommand;
import com.myapp.spring.command.CreateTransactionCommand;
import com.myapp.spring.command.RejectTransactionCommand;
import com.myapp.spring.events.TransactionApprovedEvent;
import com.myapp.spring.events.TransactionCreatedEvent;
import com.myapp.spring.events.TransactionRejectedEvent;
import com.myapp.spring.model.TransactionStatus;

@Aggregate
public class TransactionAggregate {

    @AggregateIdentifier
    private String transactionId;
    
    private String accountToId;
    private String accountFromId;

    private BigDecimal amount;

     private TransactionStatus transactionStatus;
    
    public TransactionAggregate() {
    }

    @CommandHandler
    public TransactionAggregate(CreateTransactionCommand createTransactionCommand, MetaData mData) {
        TransactionCreatedEvent transactionCreatedEvent = new TransactionCreatedEvent();
        BeanUtils.copyProperties(createTransactionCommand, transactionCreatedEvent);

        if(createTransactionCommand.getTransactionStatus().equals(TransactionStatus.REJECTED)){
            TransactionRejectedEvent transactionRejectedEvent = new TransactionRejectedEvent(createTransactionCommand.getTransactionId(),"Blocked");
            AggregateLifecycle.apply(transactionRejectedEvent);
        }
        else {
            AggregateLifecycle.apply(transactionCreatedEvent);
        }
    }

    @EventSourcingHandler
    public void on(TransactionCreatedEvent transactionCreatedEvent) throws Exception {
        this.transactionId = transactionCreatedEvent.getTransactionId();
        this.accountToId = transactionCreatedEvent.getAccountToId();
        this.accountFromId = transactionCreatedEvent.getAccountFromId();

        this.amount = transactionCreatedEvent.getAmount();

        this.transactionStatus =  transactionCreatedEvent.getTransactionStatus();

    }
    
    @CommandHandler
    public void handle(ApproveTransactionCommand approveTransactionCommand) {
    	// Create and publish the TransactionApprovedEvent
    	
    	TransactionApprovedEvent transactionApprovedEvent =
                TransactionApprovedEvent.builder()
                        .transactionId(approveTransactionCommand.getTransactionId())
                        .accountFromId(approveTransactionCommand.getAccountFromId())
                        .accountToId(approveTransactionCommand.getAccountToId())
                        .build();


    	
    	AggregateLifecycle.apply(transactionApprovedEvent);
    }
    
    @EventSourcingHandler
    public void on(TransactionApprovedEvent transactionApprovedEvent) {
    	this.transactionStatus = transactionApprovedEvent.getTransactionStatus();

    }
 
    @CommandHandler
    public void handle(RejectTransactionCommand rejectTransactionCommand) {
    	
    	TransactionRejectedEvent transactionRejectedEvent = new TransactionRejectedEvent(rejectTransactionCommand.getTransactionId(),
    			rejectTransactionCommand.getReason());
    	
    	AggregateLifecycle.apply(transactionRejectedEvent);
    	
    }
    
    @EventSourcingHandler
    public void on(TransactionRejectedEvent transactionRejectedEvent) {
    	this.transactionStatus = transactionRejectedEvent.getTransactionStatus();

    }

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAccountToId() {
		return accountToId;
	}

	public void setAccountToId(String accountToId) {
		this.accountToId = accountToId;
	}

	public String getAccountFromId() {
		return accountFromId;
	}

	public void setAccountFromId(String accountFromId) {
		this.accountFromId = accountFromId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
    
    
    

}
