package com.myapp.spring.projection;



import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.myapp.spring.domain.entity.AccountQueryEntity;
import com.myapp.spring.events.AccountCreatedEvent;
import com.myapp.spring.events.AmountCreditedEvent;
import com.myapp.spring.events.AmountDebitedEvent;
import com.myapp.spring.query.FindAccountQuery;
import com.myapp.spring.repository.AccountQueryRepository;



@Component
public class BankAccountProjection {

    private final AccountQueryRepository repository;
   
    
    
    


    public BankAccountProjection(AccountQueryRepository repository) {
		super();
		this.repository = repository;
		
	}

	@EventHandler
    public void on(AccountCreatedEvent event) {
       
        AccountQueryEntity bankAccount = new AccountQueryEntity(event.getId(),event.getInitialBalance());
        this.repository.save(bankAccount);
    }

    @EventHandler
    public void on(AmountCreditedEvent event) throws AccountNotFoundException {
      
        Optional<AccountQueryEntity> optionalBankAccount = this.repository.findById(event.getId());
        if (optionalBankAccount.isPresent()) {
        	AccountQueryEntity bankAccount = optionalBankAccount.get();
            bankAccount.setBalance(bankAccount.getBalance().add(event.getCreditAmount()));
            this.repository.save(bankAccount);
        } else {
            throw new AccountNotFoundException(event.getId());
        }
    }

    @EventHandler
    public void on(AmountDebitedEvent event) throws AccountNotFoundException {
       
        Optional<AccountQueryEntity> optionalBankAccount = this.repository.findById(event.getId());
        if (optionalBankAccount.isPresent()) {
        	AccountQueryEntity bankAccount = optionalBankAccount.get();
            bankAccount.setBalance(bankAccount.getBalance().subtract(event.getDebitAmount()));
            this.repository.save(bankAccount);
        } else {
            throw new AccountNotFoundException(event.getId());
        }
    }

    @QueryHandler
    public AccountQueryEntity handle(FindAccountQuery query) {
       
        return this.repository.findById(query.getAccountId()).orElse(null);
    }
}
