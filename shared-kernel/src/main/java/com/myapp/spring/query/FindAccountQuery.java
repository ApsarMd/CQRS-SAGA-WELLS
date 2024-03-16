package com.myapp.spring.query;

public class FindAccountQuery {
	
	private String accountId;
	
	

	public FindAccountQuery(String accountId) {
		super();
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	
	
	

}
