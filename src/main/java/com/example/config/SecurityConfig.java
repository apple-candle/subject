package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.jwt.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterchain(HttpSecurity http) throws Exception{
		http.csrf((csrf) -> csrf.disable());
		
		http.sessionManagement((session) -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.authorizeHttpRequests((auth) -> auth
					.requestMatchers("/", "/error", "/loginForm/**", "/loginProc", "/registForm", "/registProc", "/announcement").permitAll()
					.requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/fragments/**").permitAll()
					.requestMatchers("/member/**").hasRole("MEMBER")
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.anyRequest().authenticated()
		);
		
		http.exceptionHandling((exceptions) -> exceptions
				.authenticationEntryPoint(new org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint("/loginForm?error=unauthorized"))
				.accessDeniedHandler(new org.springframework.security.web.access.AccessDeniedHandlerImpl() {
			        @Override
			        public void handle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws java.io.IOException {
			            System.out.println("권한 부족! (403 Forbidden)");
			            response.sendRedirect("/loginForm?error=forbidden");
			        }
			    })
		);
		
		http.logout((logout) -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/")
	            .deleteCookies("jwt")
	            .invalidateHttpSession(true)
	    );
	
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
