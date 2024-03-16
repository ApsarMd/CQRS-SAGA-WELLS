
package com.myapp.spring.web.api;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.spring.command.CreateTransactionCommand;

import com.myapp.spring.model.TransactionStatus;
import com.myapp.spring.model.TransactionSummary;
import com.myapp.spring.query.FindTransactionQuery;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionsCommandController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsCommandController.class);
	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;


	
	public TransactionsCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}

	@PostMapping
	public ResponseEntity<String> createOrder(@Valid @RequestBody TransactionCreateRest order) {


		String transactionId = UUID.randomUUID().toString();

		CreateTransactionCommand createTransactionCommand = CreateTransactionCommand.builder()
				.accountToId(order.getAccountToId())
				.accountFromId(order.getAccountFromId())
				.amount(order.getAmount())

				.transactionId(transactionId)
				.transactionStatus(TransactionStatus.CREATED).build();

		SubscriptionQueryResult<TransactionSummary, TransactionSummary> queryResult = queryGateway.subscriptionQuery(
				new FindTransactionQuery(transactionId), ResponseTypes.instanceOf(TransactionSummary.class),
				ResponseTypes.instanceOf(TransactionSummary.class));

		try {
			String res = commandGateway.sendAndWait(createTransactionCommand);
			LOGGER.info("commandGateway.sendAndWait(createTransactionCommand) " + res);
			ObjectMapper mapper = new ObjectMapper();
			String resp = mapper.writeValueAsString(queryResult.updates());
			if(resp.length()>=1) {
				LOGGER.info("about to send RespEntity with ", resp);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			else{
				return new ResponseEntity<>("Oups! Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		catch(JsonProcessingException e){
			return new ResponseEntity<>("Oups! Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		 finally {
			queryResult.close();
		}

	}

}
