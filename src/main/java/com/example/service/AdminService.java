package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.AccountEntity;
import com.example.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final AccountRepository accountRepository;
	
	@Transactional
	public boolean toggleAccountFreeze(String username) {
		AccountEntity account = accountRepository.findByMember_Username(username)
				.orElseThrow(() -> new IllegalArgumentException("해당되는 유저를 찾을 수 없었습니다"));
		
		account.toggleFrozen();
		
		return account.isFrozen();
	}
}
