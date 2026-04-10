package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="accountdata")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String accountNumber;
	private Long balance;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;
	private boolean isFrozen = false;
	
	public void deposit(Long amount) {
		if (amount <= 0L) {
	        throw new IllegalArgumentException("입금 금액은 0원보다 커야 합니다.");
	    }
	    this.balance += amount;
	}
	
	public void withdraw(Long amount) {
		if(this.balance < amount) {
			throw new RuntimeException("잔액이 부족합니다");
		}
		this.balance -= amount;
	}
	
	public void toggleFrozen() {
		this.isFrozen = !this.isFrozen;
	}
	
	@Builder
    public AccountEntity(String accountNumber, Long balance, MemberEntity member) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.member = member;
    }
}
