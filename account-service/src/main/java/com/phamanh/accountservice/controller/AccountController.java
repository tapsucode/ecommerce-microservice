package com.phamanh.accountservice.controller;

import com.phamanh.accountservice.demo.exception.UserException;
import com.phamanh.accountservice.domains.Account;
import com.phamanh.accountservice.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/profile")
    public ResponseEntity<Account> getUserProfileHandler(@RequestHeader("Authorization") String jwt){

        return new ResponseEntity<>(accountService.findUserProfileByJwt(jwt), HttpStatus.CREATED);
    }
}
