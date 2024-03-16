package com.myapp.spring.saga;

import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.myapp.spring.command.ApproveTransactionCommand;
import com.myapp.spring.command.DepositMoneyCommand;
import com.myapp.spring.command.MoneyRollbackCommand;
import com.myapp.spring.command.RejectTransactionCommand;
import com.myapp.spring.command.ReserveMoneyCommand;
import com.myapp.spring.events.MoneyDepositedEvent;
import com.myapp.spring.events.MoneyReservedEvent;
import com.myapp.spring.events.MoneyRolledbackEvent;
import com.myapp.spring.events.TransactionApprovedEvent;
import com.myapp.spring.events.TransactionCreatedEvent;
import com.myapp.spring.events.TransactionRejectedEvent;
import com.myapp.spring.model.TransactionSummary;
import com.myapp.spring.query.FindTransactionQuery;

@Saga
public class TransactionSaga {
	
	@Autowired
	private transient CommandGateway commandGateway;
	
	
	
	@Autowired
	private transient QueryUpdateEmitter queryUpdateEmitter;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionSaga.class);
	
	
	
	@StartSaga
	@SagaEventHandler(associationProperty="transactionId")
	public void handle(TransactionCreatedEvent transactionCreatedEvent) {

		ReserveMoneyCommand reserveMoneyCommand = ReserveMoneyCommand.builder()
				.transactionId(transactionCreatedEvent.getTransactionId())
				.accountFromId(transactionCreatedEvent.getAccountFromId())
				.accountToId(transactionCreatedEvent.getAccountToId())
				.amount(transactionCreatedEvent.getAmount())
				.build();
		
		LOGGER.info("TransactionCreatedEvent handled for transactionId: " + reserveMoneyCommand.getTransactionId() +
				" and accountId: " + reserveMoneyCommand.getAccountFromId() );
		
		commandGateway.send(reserveMoneyCommand, new CommandCallback<ReserveMoneyCommand, Object>() {

			@Override
			public void onResult(CommandMessage<? extends ReserveMoneyCommand> commandMessage,
					CommandResultMessage<? extends Object> commandResultMessage) {
				   if(commandResultMessage.isExceptional()) {
					   // Start a compensating transaction
						RejectTransactionCommand rejectTransactionCommand = new RejectTransactionCommand(transactionCreatedEvent.getTransactionId(),
								commandResultMessage.exceptionResult().getMessage());
						
						commandGateway.send(rejectTransactionCommand);
				   }
			}
		});
	}
	
	@SagaEventHandler(associationProperty="transactionId")
	public void handle(MoneyReservedEvent moneyReservedEvent) {
		// Process user payment
        LOGGER.info("MoneyReserveddEvent is called for accountId: "+ moneyReservedEvent.getAccountFromId() +
        		" and transactionId: " + moneyReservedEvent.getTransactionId());


		DepositMoneyCommand depositMoneyCommand = DepositMoneyCommand.builder()
				.transactionId(moneyReservedEvent.getTransactionId())
				.accountToId(moneyReservedEvent.getAccountToId())
				.accountFromId(moneyReservedEvent.getAccountFromId())
				.amount(moneyReservedEvent.getAmount())
				.build();
        
        String result;
        try {
        result = commandGateway.sendAndWait(depositMoneyCommand, 10000, TimeUnit.MILLISECONDS );
			LOGGER.info("commandGateway.sendAndWait(DepositMoneyCommand )"+result);

        } catch(Exception ex) {
        	LOGGER.error("Saga MoneyReservedEvent after DepositMoneyCommand is sent " + ex.getMessage());
        	// Start compensating transaction
        	cancelMoneyReservation(moneyReservedEvent,ex.getMessage());
        	return;
        }
        

 
	}
	
	private void cancelMoneyReservation(MoneyReservedEvent moneyReservedEvent, String reason) {
		
		MoneyRollbackCommand moneyRollbackCommand =
				MoneyRollbackCommand.builder()
				.transactionId(moneyReservedEvent.getTransactionId())
				.accountId(moneyReservedEvent.getAccountFromId())
				.amount(moneyReservedEvent.getAmount())
				.reason(reason)
				.build();
		LOGGER.info("MoneyRollbackCommand is about to be sent, account from is "+ moneyRollbackCommand.getAccountId());
		commandGateway.send(moneyRollbackCommand);
	}


	@SagaEventHandler(associationProperty="transactionId")
	public void handle(MoneyDepositedEvent moneyDepositedEvent) {
		LOGGER.info("MoneyDepositedEvent. MoneyDepositedEvent for accounttId: " + moneyDepositedEvent.getAccountToId());


		// Send an ApproveTransactionCommand
		ApproveTransactionCommand approveTransactionCommand =
				new ApproveTransactionCommand(moneyDepositedEvent.getTransactionId()
						, moneyDepositedEvent.getAccountFromId()
						, moneyDepositedEvent.getAccountToId());

		commandGateway.send(approveTransactionCommand);
	}

	
	@EndSaga
	@SagaEventHandler(associationProperty="transactionId")
	public void handle(TransactionApprovedEvent transactionApprovedEvent) {
		LOGGER.info("Transaction is approved. Transaction Saga is complete for transactionId: " + transactionApprovedEvent.getTransactionId());

		queryUpdateEmitter.emit(FindTransactionQuery.class, query -> true,
				new TransactionSummary(transactionApprovedEvent.getTransactionId(),
						transactionApprovedEvent.getTransactionStatus(),
						""));
	}

	@SagaEventHandler(associationProperty="transactionId")
	public void handle(MoneyRolledbackEvent moneyRolledbackEvent) {
		// Create and send a RejectTransactionCommand
		RejectTransactionCommand rejectTransactionCommand = new RejectTransactionCommand(moneyRolledbackEvent.getTransactionId(),
				moneyRolledbackEvent.getReason());
		
		commandGateway.send(rejectTransactionCommand);
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty="transactionId")
	public void handle(TransactionRejectedEvent transactionRejectedEvent) {
		LOGGER.info("Successfully rejected order with id " + transactionRejectedEvent.getTransactionId());
		
		queryUpdateEmitter.emit(FindTransactionQuery.class, query -> true,
				new TransactionSummary(transactionRejectedEvent.getTransactionId(),
						transactionRejectedEvent.getTransactionStatus(),
						transactionRejectedEvent.getReason()));
	}




}
