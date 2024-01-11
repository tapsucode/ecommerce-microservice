package com.phamanh.commonservice.kafkabridge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountVerification {
    private String email;
    private String confirmURL;
}
