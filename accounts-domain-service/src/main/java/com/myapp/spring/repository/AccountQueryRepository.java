package com.myapp.spring.repository;

import org.springframework.data.repository.CrudRepository;

import com.myapp.spring.domain.entity.AccountQueryEntity;

public interface AccountQueryRepository  
extends CrudRepository<AccountQueryEntity, String>{

}
