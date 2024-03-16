package com.myapp.spring.domain.aggregate;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myapp.spring.command.CreditCommand;
import com.myapp.spring.command.DebitCommand;
import com.myapp.spring.command.DepositMoneyCommand;
import com.myapp.spring.command.MoneyRollbackCommand;
import com.myapp.spring.command.NewAccountCreateCommand;
import com.myapp.spring.command.ReserveMoneyCommand;
import com.myapp.spring.events.AccountCreatedEvent;
import com.myapp.spring.events.AmountCreditedEvent;
import com.myapp.spring.events.AmountDebitedEvent;
import com.myapp.spring.events.MoneyDepositedEvent;
import com.myapp.spring.events.MoneyReservedEvent;
import com.myapp.spring.events.MoneyRolledbackEvent;

@Aggregate
public class AccountEntityAggregate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountEntityAggregate.class);
	
	@AggregateIdentifier
	private String id;
	
	private BigDecimal balance;
	
	private Boolean isBlocked = false;
	
	private Status status;
	
	
	private Currency currency;
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getBalance() {
		return balance;
	}





	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}





	public Currency getCurrency() {
		return currency;
	}





	public void setCurrency(Currency currency) {
		this.currency = currency;
	}






	public Status getStatus() {
		return status;
	}





	public void setStatus(Status status) {
		this.status = status;
	}
	
	public AccountEntityAggregate() {
		// TODO Auto-generated constructor stub
		System.out.println("******* balance "+this.balance);
		
	}

	@CommandHandler
	public AccountEntityAggregate(NewAccountCreateCommand command) {
		System.out.println("******* aggregate "+command);
		AggregateLifecycle.apply(new AccountCreatedEvent(command.getId(), 
				command.getAccountBalance(), command.getAccountHolder()));
		
	}
	
	@EventSourcingHandler
	public void on(AccountCreatedEvent event) {
		
		this.id = event.getId();
		this.balance = event.getInitialBalance();
	}
	
	
	@CommandHandler
	public void handle(CreditCommand command) {
		System.out.println("*** credit commandhandler");
		AggregateLifecycle.apply(new AmountCreditedEvent(command.getId(), 
				command.getCreditAmount()));
		
	}
	@EventSourcingHandler
	public void on(AmountCreditedEvent event) {
		System.out.println("*** credit commandsourcinghandler");
		this.balance =this.balance.add(event.getCreditAmount());
	}
	
	@CommandHandler
	public void handle(DebitCommand command) {
		
		AggregateLifecycle.apply(new AmountDebitedEvent(command.getId(), 
				command.getDebitAmount()));
		
	}
	@EventSourcingHandler
	public void on(AmountDebitedEvent event) throws InsufficientBalanceException {
		if(this.balance.compareTo(event.getDebitAmount())<0) {
			throw new InsufficientBalanceException(event.getId(),event.getDebitAmount());
		}
		
		this.balance = this.balance.subtract(event.getDebitAmount());
	}
	
	
	@CommandHandler
	public void handle(ReserveMoneyCommand reserveMoneyCommand) {
		LOGGER.info("ReserveMoneyCommand in  productAggregateId: " + reserveMoneyCommand.getAccountFromId());
		if(balance.compareTo( reserveMoneyCommand.getAmount())==-1) {
			LOGGER.info("Insufficient funds on the account "+this.id);
			throw new IllegalArgumentException("Insufficient funds on the account "+this.id);
		}
		if(isBlocked == true) {
			LOGGER.info("The from account is blocked: "+this.id);
			throw new IllegalArgumentException("The from account is blocked: "+this.id);
		}
		MoneyReservedEvent moneyReservedEvent = MoneyReservedEvent.builder()
				.transactionId(reserveMoneyCommand.getTransactionId())
				.accountFromId(reserveMoneyCommand.getAccountFromId())
				.accountToId(reserveMoneyCommand.getAccountToId())
				.amount(reserveMoneyCommand.getAmount())
				.build();

		AggregateLifecycle.apply(moneyReservedEvent);
		
	}

	@CommandHandler
	public void handle(DepositMoneyCommand depositMoneyCommand) {

		LOGGER.info("DepositMoneyCommand in  productAggregateId: " + depositMoneyCommand.getAccountToId());

		if(isBlocked == true) {
			throw new IllegalArgumentException("The to account is blocked: "+this.id);
		}

		MoneyDepositedEvent moneyDepositedEvent = MoneyDepositedEvent.builder()
				.transactionId(depositMoneyCommand.getTransactionId())
				.accountFromId(depositMoneyCommand.getAccountFromId())
				.accountToId(depositMoneyCommand.getAccountToId())
				.amount(depositMoneyCommand.getAmount())
				.build();

		AggregateLifecycle.apply(moneyDepositedEvent);

	}
	
	@CommandHandler
	public void handle(MoneyRollbackCommand moneyRollbackCommand) {
		
		MoneyRolledbackEvent moneyRolledbackEvent =
				MoneyRolledbackEvent.builder()
				.transactionId(moneyRollbackCommand.getTransactionId())
				.accountId(moneyRollbackCommand.getAccountId())
				.amount(moneyRollbackCommand.getAmount())
				.reason(moneyRollbackCommand.getReason())
				.build();
		
		AggregateLifecycle.apply(moneyRolledbackEvent);
		
	}
	
	
	@EventSourcingHandler
	public void on(MoneyRolledbackEvent moneyRolledbackEvent) {
		this.balance = this.balance.add(moneyRolledbackEvent.getAmount());
		LOGGER.info("MoneyRolledbackEvent in  productAggregateId: " + this.id +
				" and the quantity is "+ this.balance
		);
	}




	@Override
	public String toString() {
		return "AccountEntityAggregate [id=" + id + ", balance=" + balance + ", status=" + status + ", currency="
				+ currency + "]";
	}






	private enum Currency{
		INR,USD
	}
	
	private enum Status{
		HOLD,COMPLETED
	}

}
