package com.phamanh.notificationservice.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class MailInfo extends SimpleMailInfo {

    private final Set<String> cc = new HashSet<>();

    private final Set<String> bcc = new HashSet<>();

    private boolean html;

}
