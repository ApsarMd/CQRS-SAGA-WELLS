package com.myapp.spring.web.api;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionCreateRest {

    @NotBlank(message = "AccountFromId is a required field")
    private String accountFromId;

    @NotBlank(message = "AccountToId is a required field")
    private String accountToId;
    

    @Max(value = 500, message = "Amount cannot be larger than 500")
    private BigDecimal amount;
    

    private String relation;
    
}
