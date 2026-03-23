package com.CardMaster.modules.cias.controller;

import com.CardMaster.modules.cias.dto.CardAccountRequestDto;
import com.CardMaster.modules.cias.dto.CardAccountResponseDto;
import com.CardMaster.modules.cias.mapper.CardAccountMapper;
import com.CardMaster.modules.cias.entity.CardAccount;
import com.CardMaster.modules.cias.service.AccountSetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountSetupController {

    private final AccountSetupService accountService;
    private final CardAccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<CardAccountResponseDto> createAccount(@RequestBody CardAccountRequestDto request) {
        CardAccount account = accountService.createAccount(request);
        return ResponseEntity.ok(accountMapper.toDTO(account));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<CardAccountResponseDto> getAccount(@PathVariable Long accountId) {
        CardAccount account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(accountMapper.toDTO(account));
    }

    @GetMapping("/my")
    public ResponseEntity<CardAccountResponseDto> getMyAccount(Principal principal) {
        CardAccount account = accountService.getAccountByEmail(principal.getName());
        return ResponseEntity.ok(accountMapper.toDTO(account));
    }


    @PostMapping("/use/{accountId}")
    public ResponseEntity<CardAccountResponseDto> useCard(
            @PathVariable Long accountId,
            @RequestParam Double amount) {
        CardAccount account = accountService.useCard(accountId, amount);
        return ResponseEntity.ok(accountMapper.toDTO(account));
    }
}
