package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dto.TransactionDto;
import com.example.entity.AccountEntity;
import com.example.entity.TransactionEntity;
import com.example.service.AccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/main")
    public String mainPage(Model model, Principal principal) {
        AccountEntity account = accountService.getAccountInfo(principal.getName());
        model.addAttribute("account", account);
        return "member/main";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("amount") Long amount, Principal principal, RedirectAttributes rttr) {
        if (amount <= 0L) {
            rttr.addFlashAttribute("errorMessage", "입금 금액은 0원보다 커야 합니다.");
            return "redirect:/member/main";
        }
        accountService.deposit(principal.getName(), amount);
        rttr.addFlashAttribute("successMessage", "입금이 완료되었습니다.");
        return "redirect:/member/main";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("receiverAccount") String receiverAccount, 
    					   @RequestParam("amount") Long amount, Principal principal, RedirectAttributes rttr) {
        try {
            accountService.transfer(principal.getName(), receiverAccount, amount);
            rttr.addFlashAttribute("successMessage", "송금이 성공적으로 완료되었습니다.");
        } catch (RuntimeException e) {
            rttr.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/member/main";
    }

    @GetMapping("/history")
    public String getHistory(Model model, Principal principal) {
        List<TransactionDto> logs = accountService.getRecentTransactions(principal.getName());
        model.addAttribute("logs", logs);
        return "member/history";
    }
}