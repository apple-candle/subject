package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.LoginDto;
import com.example.dto.MemberDto;
import com.example.entity.AccountEntity;
import com.example.entity.MemberEntity;
import com.example.jwt.JwtUtill;
import com.example.repository.AccountRepository;
import com.example.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtill jwtUtill;
	
	public void joinMember(MemberDto memberDto) {
		
		if(memberRepository.existsByUsername(memberDto.getUsername())) {
	        throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
	    }
		
		MemberEntity member = MemberEntity.builder()
					.username(memberDto.getUsername())
					.password(passwordEncoder.encode(memberDto.getPassword()))
					.role(MemberEntity.RoleType.ROLE_MEMBER)
					.build();
		
		MemberEntity savedMember = memberRepository.save(member);
		
		String newAccountNumber = generateAccountNumber();
		
		AccountEntity account = AccountEntity.builder()
				.accountNumber(newAccountNumber)
				.balance(0L)
				.member(savedMember)
				.build();
		
		accountRepository.save(account);
	}
	
	private String generateAccountNumber() {
        return "BNK-" + System.currentTimeMillis() % 100000000;
    }
	
	public String Userlogin(LoginDto loginDto) throws IllegalAccessException{
		
		MemberEntity member = memberRepository.findByUsername(loginDto.getUsername())
				.orElseThrow(() -> new IllegalAccessException("존재하지 않는 계정입니다")); 
		
		if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		return jwtUtill.generateToken(member.getUsername(), member.getRole().name());
	}
	
	public List<MemberDto> allUser(){
		List<MemberEntity> userList = memberRepository.findAll();
		List<MemberDto> dtoList = new ArrayList<>();
		
		for(MemberEntity members : userList) {
			String accNum = "계좌 없음";
			Long accBalance = 0L;
	        AccountEntity account = accountRepository.findByMember_Username(members.getUsername()).orElse(null);
	        if (account != null) {
	            accNum = account.getAccountNumber();
	            accBalance = account.getBalance();
	        }
			
			MemberDto memberDto = MemberDto.builder()
					.id(members.getId())
					.username(members.getUsername())
					.password(members.getPassword())
					.role(members.getRole().name())
					.accountNumber(accNum)
					.balance(accBalance)
					.build();
	        dtoList.add(memberDto);
		}
		
		return dtoList;
	}
}
