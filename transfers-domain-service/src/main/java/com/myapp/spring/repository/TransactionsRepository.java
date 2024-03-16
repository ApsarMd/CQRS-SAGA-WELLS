
package com.myapp.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.spring.domain.entity.TransactionEntity;

public interface TransactionsRepository extends JpaRepository <TransactionEntity, String>{
	TransactionEntity findByTransactionId(String orderId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM ASSOCIATION_VALUE_ENTRY;" , nativeQuery = true)
	void deleteAllAssociateTables();

	@Transactional
	@Modifying
	@Query(value =
			"DELETE FROM SAGA_ENTRY;"
			, nativeQuery = true)
	void deleteAllSagaTables();

	@Transactional
	@Modifying
	@Query(value =
			"DELETE FROM TOKEN_ENTRY;", nativeQuery = true)
	void deleteAllTokenTables();

}
