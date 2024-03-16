package com.myapp.spring.service;

import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import com.myapp.spring.command.CreditCommand;
import com.myapp.spring.command.DebitCommand;
import com.myapp.spring.command.NewAccountCreateCommand;
import com.myapp.spring.domain.entity.AccountQueryEntity;
import com.myapp.spring.dto.AccountCreateDTO;
import com.myapp.spring.dto.AmountDTO;

@Service
public class BankService {

	private CommandGateway commandGateway;
	
	public BankService(CommandGateway commandGateway) {
		
		this.commandGateway = commandGateway;
	}

	public CompletableFuture<AccountQueryEntity> createAccount(AccountCreateDTO creationDTO) {
		return commandGateway.send(new NewAccountCreateCommand(creationDTO.getId()
				, creationDTO.getInitialBalance(),creationDTO.getOwner()));
		
	}
	
public CompletableFuture<String> credit(String accountId,AmountDTO dto) {
	return commandGateway.send(new CreditCommand(accountId, dto.getAmount()));
		
	}

public CompletableFuture<String> debit(String accountId,AmountDTO dto) {
	return commandGateway.send(new DebitCommand(accountId, dto.getAmount()));
}
}
