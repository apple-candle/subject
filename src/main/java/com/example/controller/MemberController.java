package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dto.LoginDto;
import com.example.dto.MemberDto;
import com.example.jwt.JwtUtill;
import com.example.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	JwtUtill jwtUtill;
	
	@GetMapping("/")
	public String root() {
		return "index";
	}
	
	@GetMapping("/announcement")
	public String rootAnnouncement() {
		return "announcement";
	}
	
	@GetMapping("/loginForm")
	public String rootLoginForm(Authentication auth) {
		if(auth != null && auth.isAuthenticated()) {
			return "redirect:/";
		}
		return "loginForm";
	}
	
	@PostMapping("loginProc")
	public String activeLoginProc(LoginDto loginDto, HttpServletResponse response, RedirectAttributes rttr) throws IllegalAccessException{
		
		try {
	        String token = memberService.Userlogin(loginDto);
	        String role = jwtUtill.getRole(token);

	        Cookie cookie = new Cookie("jwt", token);
	        cookie.setHttpOnly(true);
	        cookie.setPath("/");
	        cookie.setMaxAge(60 * 60);
	        response.addCookie(cookie);
	        
	        if("ROLE_ADMIN".equals(role)) {
	        	return "redirect:/admin/main";
	        }
	        else {
	        	return "redirect:/member/main";
	        }

	    } catch (IllegalArgumentException | IllegalAccessException e) {
	        rttr.addFlashAttribute("errorMessage", e.getMessage());
	        return "redirect:/loginForm";
	    }
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletResponse response) {
	    Cookie cookie = new Cookie("jwt", null);
	    
	    cookie.setMaxAge(0);
	    cookie.setPath("/");

	    response.addCookie(cookie);
	    
	    return "redirect:/loginForm";
	}
	
	@GetMapping("/registForm")
	public String rootRegistForm(Authentication auth) {
		if (auth != null && auth.isAuthenticated()) {
            return "redirect:/"; 
        }
		return "registForm";
	}
	
	@PostMapping("/registProc")
	public String activeRegistProc(MemberDto memberDto, RedirectAttributes rttr) {
		
		try {
	        memberService.joinMember(memberDto);
	        rttr.addFlashAttribute("successMessage", "회원가입이 완료되었습니다!");
	        return "redirect:/loginForm";
	        
	    } catch (IllegalArgumentException e) {
	        rttr.addFlashAttribute("errorMessage", e.getMessage());
	        return "redirect:/registForm";
	    }
	}
}
