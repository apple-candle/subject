package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dto.MemberDto;
import com.example.service.AdminService;
import com.example.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
	
	@Autowired
	MemberService memberService;

	@GetMapping("/main")
	public String rootMain(Model model) {
		List<MemberDto> userList = memberService.allUser();
		model.addAttribute("members", userList);
		return "admin/main";
	}
	
	@PostMapping("/freeze")
	public String freezeProc(@RequestParam("username")String username, RedirectAttributes rttr) {
		try {
	        boolean currentStatus = adminService.toggleAccountFreeze(username);
	        
	        String action = currentStatus ? "동결" : "해제";
	        rttr.addFlashAttribute("successMessage", username + "님의 계좌가 " + action + "되었습니다.");
	        
	    } catch (Exception e) {
	        rttr.addFlashAttribute("errorMessage", e.getMessage());
	    }
	    return "redirect:/admin/main";
	}
}
