package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>{

	@Query("SELECT t FROM TransactionEntity t WHERE t.sender_account = :account OR t.receiver_account = :account ORDER BY t.createdAt DESC")
	List<TransactionEntity> findTop5SenderAndReceiver(
			@Param("account") String ccount,
			Pageable pageable
	);
}
