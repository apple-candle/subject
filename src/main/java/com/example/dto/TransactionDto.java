package com.example.dto;

import java.time.LocalDateTime;

import com.example.entity.TransactionEntity.TransactionType;

import lombok.Builder;
import lombok.Data;

@Data
public class TransactionDto {

	 private String senderName;
	 private String receiverName;
	 private Long amount;
	 private TransactionType type;
	 private LocalDateTime createdAt;
	 
	 @Builder
	 public TransactionDto(String senderName, String receiverName, Long amount, TransactionType type, LocalDateTime createdAt) {
		 this.senderName = senderName;
		 this.receiverName = receiverName;
		 this.amount = amount;
		 this.type = type;
		 this.createdAt = createdAt;
	 }
}
