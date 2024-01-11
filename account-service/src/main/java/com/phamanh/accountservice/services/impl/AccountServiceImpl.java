package com.phamanh.accountservice.services.impl;



import com.phamanh.accountservice.demo.exception.UserException;
import com.phamanh.accountservice.demo.model.User;
import com.phamanh.accountservice.domains.Account;
import com.phamanh.accountservice.domains.ConfirmationCode;
import com.phamanh.accountservice.domains.Role;
import com.phamanh.accountservice.dtos.request.AccountRegistrationRequest;
import com.phamanh.accountservice.dtos.response.AccountById;
import com.phamanh.accountservice.dtos.response.AccountRegistrationResponse;
import com.phamanh.accountservice.repositories.AccountRepository;
import com.phamanh.accountservice.repositories.RoleRepository;
import com.phamanh.accountservice.repositories.UserConfirmationRepository;
import com.phamanh.accountservice.security.JwtProvider;
import com.phamanh.accountservice.services.AccountService;
import com.phamanh.commonservice.exceptions.ResourceConflictException;
import com.phamanh.commonservice.exceptions.ResourceNotFoundException;
import com.phamanh.commonservice.kafkabridge.AccountVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final StreamBridge streamBridge;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final UserConfirmationRepository userConfirmationRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    private static final String ACCOUNT_CONFIRM_TOPIC = "accountConfirm-out-0";
    private static final String CREATE_CART_TOPIC = "createCart-out-0";


    @Override
    public AccountRegistrationResponse registerUser(AccountRegistrationRequest accountRegistrationRequest) {

        // Check if the email and username exist in the database
        if(accountRepository.findByUsername(accountRegistrationRequest.getUsername()).isPresent()){
            throw new ResourceConflictException("User with the provided username already exists.");
        }

        if(accountRepository.findByEmail(accountRegistrationRequest.getEmail()).isPresent()){
            throw new ResourceConflictException("User with the provided email already exists.");
        }
        // Create confirmation code,set account information and save account in database
        String code = UUID.randomUUID().toString();
        Account account = new Account();
        BeanUtils.copyProperties(accountRegistrationRequest, account);
        account.setStatus(Account.Status.PENDING);
        account.setCreateAt(LocalDateTime.now());
        Role role = roleRepository.findByName(Role.Name.CUSTOMER).orElseThrow();
        List<Role> listRole = new ArrayList<>();
        listRole.add(role);
        account.setRoles(listRole);
        account.setPassword(passwordEncoder.encode(accountRegistrationRequest.getPassword()));

        AccountRegistrationResponse accountRegistrationResponse = new AccountRegistrationResponse();
        ConfirmationCode userConfirmation = new ConfirmationCode(null, code, ConfirmationCode.Status.USED, account);

        Account saveAccount = accountRepository.save(account);

        BeanUtils.copyProperties(saveAccount, accountRegistrationResponse);
        userConfirmationRepository.save(userConfirmation);

        String confirmURL =String.format("/api/v1/auth/confirm/%s/%s", accountRegistrationResponse.getId(),code);

        AccountVerification accountVerification = new AccountVerification(accountRegistrationResponse.getEmail(),confirmURL);

        // Send email information and confirmURL to notification-service
        streamBridge.send(ACCOUNT_CONFIRM_TOPIC,accountVerification);

        streamBridge.send(CREATE_CART_TOPIC,saveAccount.getId());


        return accountRegistrationResponse;
    }

    @Override
    public boolean confirmUser(Long accountId, String code) {
        // Check if the account id and confirmation code exist in the database
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        ConfirmationCode confirmationCode = userConfirmationRepository
                .findByAccountIdAndCodeAndStatus(accountId, code, null)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation code not found"));

        confirmationCode.setStatus(ConfirmationCode.Status.EXPIRED);
        userConfirmationRepository.save(confirmationCode);
        account.setStatus(Account.Status.ACTIVATED);
        accountRepository.save(account);

        return true;
    }

    @Override
    public AccountById getAccountById(Long accountId) {

        AccountById accountById = new AccountById();

        BeanUtils.copyProperties( accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException(String.format("Account with accountId :%s does not exist",accountId))),accountById);
        return  accountById;
    }


    @Override
    public Account findUserProfileByJwt(String jwt) {

        String username = jwtProvider.getUsernameFromToken(jwt);

        return accountRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

}
