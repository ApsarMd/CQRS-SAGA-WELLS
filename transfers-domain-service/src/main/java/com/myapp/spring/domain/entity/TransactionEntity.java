
package com.myapp.spring.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.myapp.spring.model.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "dddtransactions")
public class TransactionEntity implements Serializable {

    private static final long serialVersionUID = 5313493413859894403L;

    @Id
    @Column(unique = true)
    public String transactionId;
    private String accountToId;
    private String accountFromId;
 
    private BigDecimal amount;
 


    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

  
   
    @CreationTimestamp
    private LocalDateTime created;

  
   
    @UpdateTimestamp
    private LocalDateTime updated;


}
