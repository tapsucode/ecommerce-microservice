package com.phamanh.accountservice.dtos.request;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegistrationRequest {

    @NotBlank
    @Size(min = 8,max = 32)
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @NotBlank
    private String address;

    @NotBlank
    private String phoneNumber;

}
