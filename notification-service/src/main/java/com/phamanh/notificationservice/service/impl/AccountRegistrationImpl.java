package com.phamanh.notificationservice.service.impl;


import com.phamanh.notificationservice.data.MailInfo;
import com.phamanh.notificationservice.service.AccountRegistration;
import com.phamanh.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountRegistrationImpl implements AccountRegistration {

    private static final String EMAIL_REGISTRATION_SUCCESSFUL_CONTENT ="email.registration.successful.content";
    private static final String EMAIL_REGISTRATION_SUCCESSFUL_SUBJECT = "email.registration.successful.subject";

    private final EmailService emailService;

    private final MessageSource messageSource;
    @Override
    public void confirmAccount(String email, String confirmURL) {

        Locale locale = LocaleContextHolder.getLocale();
        // Set values in the HTML form 'form-send-email-register-user'
        Map<String,Object> data = new HashMap<>();

        data.put("content",messageSource.getMessage(EMAIL_REGISTRATION_SUCCESSFUL_CONTENT,null,locale));
        data.put("confirmURL",confirmURL);

        // Send email using the template form

        MailInfo mailInfo = new MailInfo();
        mailInfo.addTo(email);
        mailInfo.setSubject(messageSource.getMessage(EMAIL_REGISTRATION_SUCCESSFUL_SUBJECT,null,locale));
        mailInfo.setHtml(true);
        emailService.sendHtmlMail(mailInfo,"form-send-email-register-user",data);

    }
}
