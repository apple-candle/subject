package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="transactiondata")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long id;
	 private String sender_account;
	 private String receiver_account;
	 private Long amount;
	 @Enumerated(EnumType.STRING)
	 private TransactionType type;
	 private LocalDateTime createdAt;
	 
	 public enum TransactionType {
		 DEPOSIT,
		 TRANSFER
	 }
}
