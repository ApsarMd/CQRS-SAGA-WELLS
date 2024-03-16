package com.myapp.spring.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import com.myapp.spring.domain.entity.AccountQueryEntity;
import com.myapp.spring.query.FindAccountQuery;

@Service
public class BankQueryService {
	private final EventStore eventStore;
	private final QueryGateway queryGateway;
	
	
	
	
	
public BankQueryService(EventStore eventStore, QueryGateway queryGateway) {
		super();
		this.eventStore = eventStore;
		this.queryGateway = queryGateway;
	}

public CompletableFuture<AccountQueryEntity> findById(String accountId) {
		return queryGateway.query(new FindAccountQuery(accountId),
				ResponseTypes.instanceOf(AccountQueryEntity.class));
	}

public List<Object> listEventsOccured(String accountId){
	return eventStore.readEvents(accountId.toString())
			.asStream().map(Message::getPayload).collect(Collectors.toList());
	
}


}
