package com.myapp.spring.command;

import org.axonframework.config.ProcessingGroup;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("account-group")
public class TransactionLookupEventsHandler {

	
}
