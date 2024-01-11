package com.phamanh.accountservice.dtos.response;

import com.phamanh.accountservice.domains.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDto {

    protected Long id;

    protected String username;

    protected String email;

    protected String fullName;

    protected String address;

    protected String phoneNumber;

    protected Account.Status status;
}
