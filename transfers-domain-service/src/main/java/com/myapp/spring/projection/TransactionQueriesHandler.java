package com.myapp.spring.projection;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.myapp.spring.domain.entity.TransactionEntity;
import com.myapp.spring.model.TransactionSummary;
import com.myapp.spring.query.FindTransactionQuery;
import com.myapp.spring.repository.TransactionsRepository;

@Component
public class TransactionQueriesHandler {

	TransactionsRepository transactionsRepository;

	public TransactionQueriesHandler(TransactionsRepository transactionsRepository) {
		this.transactionsRepository = transactionsRepository;
	}

	@QueryHandler
	public TransactionSummary findOrder(FindTransactionQuery findTransactionQuery) {
		TransactionEntity transactionEntity = transactionsRepository.findByTransactionId(findTransactionQuery.getTransactionId());
		return new TransactionSummary(transactionEntity.getTransactionId(),
				transactionEntity.getTransactionStatus(), "");
	}



}
