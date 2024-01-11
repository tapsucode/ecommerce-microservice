package com.phamanh.accountservice.controller;



import com.phamanh.accountservice.security.JwtProvider;
import com.phamanh.accountservice.dtos.request.SignInFrom;
import com.phamanh.accountservice.demo.response.AuthResponse;
import com.phamanh.accountservice.dtos.request.AccountRegistrationRequest;
import com.phamanh.accountservice.dtos.response.AccountRegistrationResponse;
import com.phamanh.accountservice.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("/**")
public class AuthController {

    private final AccountService accountService;
    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;
    @PostMapping("signup")
    public AccountRegistrationResponse registerUser(@Valid @RequestBody AccountRegistrationRequest accountRegistrationRequest){
        return accountService.registerUser(accountRegistrationRequest);
    }

    @PostMapping("/confirm/{userId}/{code}")
    public void confirmAccount(@PathVariable Long userId, @PathVariable("code") String confirmationCode){
        accountService.confirmUser(userId,confirmationCode);
    }

    @PostMapping("signin")
    public ResponseEntity<AuthResponse> loginUserHandle(@Valid @RequestBody SignInFrom signInFrom) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInFrom.getUsername(), signInFrom.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(token,"Signin Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);

    }

}
