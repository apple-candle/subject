package com.example.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private final JwtUtill jwtUtill;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = null;
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
	        for(Cookie cookie : cookies) {
	            if ("jwt".equals(cookie.getName())) {
	                token = cookie.getValue();
	                break;
	            }
	        }
	    }
		if (token == null || !jwtUtill.isValid(token)) {
	        filterChain.doFilter(request, response);
	        return;
	    }

		try {
			System.out.println("1. 토큰 발견 여부: " + (token != null));
			String username = jwtUtill.getUsername(token);
			String role = jwtUtill.getRole(token);
			System.out.println("2. 토큰 아이디: " + username);
			System.out.println("3. 토큰 권한: " + role);
	        
	        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
	        
	        UsernamePasswordAuthenticationToken authentication = 
	                new UsernamePasswordAuthenticationToken(username, null, authorities);
	        
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        
	    } catch (Exception e) {
	        logger.error("Security Context에 인증 정보를 설정할 수 없습니다", e);
	    }
	    
	    filterChain.doFilter(request, response);
	}
}
