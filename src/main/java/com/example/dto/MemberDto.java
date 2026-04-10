package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

	private Long id;
	private String username;
	private String password;
	private String role;
	private String accountNumber;
	private Long balance;
	private boolean isFrozen;
}
