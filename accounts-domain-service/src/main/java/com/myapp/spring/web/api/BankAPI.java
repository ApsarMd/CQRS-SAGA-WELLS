package com.myapp.spring.web.api;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.spring.domain.entity.AccountQueryEntity;
import com.myapp.spring.dto.AccountCreateDTO;
import com.myapp.spring.dto.AmountDTO;
import com.myapp.spring.service.BankService;

@RestController

@RequestMapping("/api/accounts")
public class BankAPI {
	
	
	private BankService service;

	public BankAPI(BankService service) {
		super();
		this.service = service;
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public CompletableFuture<AccountQueryEntity> createNewAccount(
			 @RequestBody AccountCreateDTO dto){
		
		return service.createAccount(dto);
		
	}
	
	@PutMapping(value = "/credit/{accountId}")
	@ResponseStatus(code = HttpStatus.OK)
	public CompletableFuture<String> creditAccount(
			@PathVariable("accountId") String accountId, @RequestBody AmountDTO dto){
		
		return service.credit(accountId, dto);
		
	}
	
	@PutMapping(value = "/debit/{accountId}")
	@ResponseStatus(code = HttpStatus.OK)
	public CompletableFuture<String> debitAccount(
			@PathVariable("accountId") String accountId, @RequestBody AmountDTO dto){
		
		return service.debit(accountId, dto);
		
	}
	
	

}
