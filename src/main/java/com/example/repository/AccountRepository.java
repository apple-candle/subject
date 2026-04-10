package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>{

	Optional<AccountEntity> findByAccountNumber(String receiverAccountNumber);
	Optional<AccountEntity> findByMember_Username(String username);
}
