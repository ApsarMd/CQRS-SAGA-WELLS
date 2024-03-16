package com.myapp.spring.web.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.spring.domain.entity.AccountQueryEntity;
import com.myapp.spring.service.BankQueryService;

@RestController
@RequestMapping("/api/accounts")
public class BankQueryAPI {
	
	private final BankQueryService bankQueryService;

	public BankQueryAPI(BankQueryService bankQueryService) {
		super();
		this.bankQueryService = bankQueryService;
	}
	
	@GetMapping("/{accountId}")
	public CompletableFuture<AccountQueryEntity> findById( 
			@PathVariable("accountId") String accountd){
		return bankQueryService.findById(accountd);
	}
	
	@GetMapping("/{accountId}/events")
	public List<Object> getEvents( 
			@PathVariable("accountId") String accountd){
		return bankQueryService.listEventsOccured(accountd);
	}

}
