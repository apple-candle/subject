package com.example.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtill {

	private final SecretKey secretKey;
	private final long expiration;
	
	public JwtUtill(@Value("${jwt.secret}") String secret,
				    @Value("${jwt.expiration}") long expiration) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expiration = expiration;
	}
	
	// 토큰 생성
	public String generateToken(String username, String role) {
		return Jwts.builder()
				   .subject(username)
				   .claim("role", role)
				   .issuedAt(new Date())
				   .expiration(new Date(System.currentTimeMillis() + expiration))
				   .signWith(secretKey)
				   .compact();
	}
	
	// 토큰 파싱
	public Claims parseToken(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}
	
	//유효성 검사
	public boolean isValid(String token) {
		
		try {
			parseToken(token);
			return true;
		}catch(JwtException | IllegalArgumentException e) {
			return false;
		}
	}
	
	//Claims에서 username 꺼내기
	public String getUsername(String token) {
		return parseToken(token).getSubject();
	}
	
	//Claims에서 role 꺼내기
	public String getRole(String token) {
		return parseToken(token).get("role", String.class);
	}
}
