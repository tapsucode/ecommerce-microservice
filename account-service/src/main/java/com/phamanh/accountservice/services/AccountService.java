package com.phamanh.accountservice.services;



import com.phamanh.accountservice.domains.Account;
import com.phamanh.accountservice.dtos.request.AccountRegistrationRequest;
import com.phamanh.accountservice.dtos.response.AccountById;
import com.phamanh.accountservice.dtos.response.AccountRegistrationResponse;

public interface AccountService {

    AccountRegistrationResponse registerUser(AccountRegistrationRequest accountRegistrationRequest);

    boolean confirmUser(Long userId,String confirmationCode);

    AccountById getAccountById(Long accountId);

    Account findUserProfileByJwt(String jwt);
}
