package com.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.TransactionDto;
import com.example.entity.AccountEntity;
import com.example.entity.TransactionEntity;
import com.example.repository.AccountRepository;
import com.example.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	
	
	@Transactional(readOnly = true)
	public AccountEntity getAccountInfo(String name) {
		return accountRepository.findByMember_Username(name)
				.orElseThrow(() -> new RuntimeException("사용자의 계좌 정보를 찾을 수 없습니다"));
	}
	
	public void transfer(String senderUsername, String receiverAccountNumber, Long amount) {
        
        AccountEntity sender = accountRepository.findByMember_Username(senderUsername)
                .orElseThrow(() -> new RuntimeException("보내는 분의 계좌를 찾을 수 없습니다."));

        AccountEntity receiver = accountRepository.findByAccountNumber(receiverAccountNumber)
                .orElseThrow(() -> new RuntimeException("받는 분의 계좌번호가 올바르지 않습니다."));

        if (sender.getBalance() < amount) {
            throw new RuntimeException("잔액이 부족합니다. 송금할 수 없습니다.");
        }

        sender.withdraw(amount);
        receiver.deposit(amount);

        TransactionEntity history = TransactionEntity.builder()
                .sender_account(String.valueOf(sender.getAccountNumber()))
                .receiver_account(String.valueOf(receiver.getAccountNumber()))
                .amount(amount)
                .type(TransactionEntity.TransactionType.TRANSFER)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(history);
    }

	public void deposit(String name, Long amount) {
		AccountEntity account = accountRepository.findByMember_Username(name)
	            .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다."));

	    account.deposit(amount);
		
	    TransactionEntity transaction = TransactionEntity.builder()
	            .receiver_account(account.getAccountNumber())
	            .sender_account("CASH")
	            .amount(amount)
	            .type(TransactionEntity.TransactionType.DEPOSIT)
	            .createdAt(LocalDateTime.now())
	            .build();

	    transactionRepository.save(transaction);
	}

	public List<TransactionDto> getRecentTransactions(String name) {
		AccountEntity myAccount = accountRepository.findByMember_Username(name)
	            .orElseThrow(() -> new RuntimeException("계좌 정보를 찾을 수 없습니다."));
		String myAccountNumber = myAccount.getAccountNumber();
		Pageable limitFive = PageRequest.of(0, 5);
		
		List<TransactionEntity> entitylogs = transactionRepository.findTop5SenderAndReceiver(myAccountNumber, limitFive);
		
		List<TransactionDto> dtoList = new ArrayList<>();
		
		for(TransactionEntity entity : entitylogs) {
			String senderName = "알 수 없음";
			
			if("CASH".equals(entity.getSender_account())) {
				senderName = "현금 입금";
			}
			else {
				AccountEntity senderAccount = accountRepository.findByAccountNumber(entity.getSender_account()).orElse(null);
	            if (senderAccount != null) {
	                senderName = senderAccount.getMember().getUsername();
	            }
			}
			
			String receiverName = "알 수 없음";
			AccountEntity receiverAccount = accountRepository.findByAccountNumber(entity.getReceiver_account()).orElse(null);
			if(receiverAccount != null) {
				receiverName = receiverAccount.getMember().getUsername();
			}
			TransactionDto dto = TransactionDto.builder()
	                .createdAt(entity.getCreatedAt())
	                .type(entity.getType())
	                .amount(entity.getAmount())
	                .senderName(senderName)
	                .receiverName(receiverName)
	                .build();
	        
	        dtoList.add(dto);
		}
		return dtoList;
	}
}
