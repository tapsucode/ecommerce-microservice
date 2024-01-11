package com.phamanh.accountservice.dtos.response;

import com.phamanh.accountservice.domains.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountRegistrationResponse extends AccountDto {

    public AccountRegistrationResponse(Long id, String username, String email, String fullName, String address, String phoneNumber, Account.Status status) {
        super(id, username, email, fullName, address, phoneNumber, status);
    }
}
