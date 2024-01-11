package com.phamanh.accountservice.controller;

import com.phamanh.accountservice.dtos.response.AccountById;
import com.phamanh.accountservice.dtos.response.ProfileAccountResponse;
import com.phamanh.accountservice.services.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/admin/account")
@RequiredArgsConstructor
public class AdminAccountController {

    private final AccountService accountService;

    @GetMapping("/{accountId}")
    public AccountById getAccountById(@NotNull @NotBlank @PathVariable(name = "accountId") Long accountId ){
        return accountService.getAccountById(accountId);
    }

    @GetMapping("/profile/{accountId}")
    public ResponseEntity<ProfileAccountResponse> getProfileById(@NotNull @NotBlank @PathVariable(name = "accountId") Long accountId ){

        ProfileAccountResponse profileAccountResponse = new ProfileAccountResponse();
        profileAccountResponse.setUsername(accountService.getAccountById(accountId).getUsername());
        profileAccountResponse.setEmail(accountService.getAccountById(accountId).getEmail());
        return new ResponseEntity<>(profileAccountResponse, HttpStatus.OK)  ;
    }
}
