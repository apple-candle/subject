package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="memberdata")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true, nullable = false)
	private String username;
	private String password;
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
	public enum RoleType{
		ROLE_ADMIN,
		ROLE_MEMBER
	}
	
	@Builder
	public MemberEntity(String username, String password, RoleType role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
}
