package com.phamanh.accountservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileAccountResponse {

    private String username;

    private String email;
}
