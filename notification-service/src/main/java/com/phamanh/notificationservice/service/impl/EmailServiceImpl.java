package com.phamanh.notificationservice.service.impl;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phamanh.notificationservice.data.MailInfo;
import com.phamanh.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:mail/application.yml")
public class EmailServiceImpl implements EmailService {
    private static final String DEFAULT_SUBJECT_CHARSET = StandardCharsets.UTF_8.name();

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    @Value("${mail/username}")
    private final String MAIL_INFO_FROM;

    @Async
    @Override
    public void sendHtmlMail(MailInfo mailInfo, String mailTemplate, Map<String,Object> data){

        // create information to MimeMessage,send html mail class MimeMessageHelper

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    DEFAULT_SUBJECT_CHARSET);
            Locale locale = LocaleContextHolder.getLocale();
            Context context = new Context(locale);

            Map<String,Object> mailVariables = this.convertObjectToMap(data);
            String mailContent = this.replaceThymeleafMarker(mailTemplate, context, mailVariables);

            mailInfo.setContent(mailContent);
            helper.setFrom(MAIL_INFO_FROM);
            helper.setTo(mailInfo.getTo());
            helper.setSubject(mailInfo.getSubject());
            helper.setText(mailInfo.getContent(),mailInfo.isHtml());
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    private <T> Map<String, Object> convertObjectToMap(T data) {
        return (data==null)?new HashMap<>():new ObjectMapper().convertValue(data, new TypeReference<>() {}) ;
    }

    private String replaceThymeleafMarker(String thymeleafMarker, Context context, Map<String, Object> mailVariables) {
        context.setVariables(mailVariables);
        return templateEngine.process(thymeleafMarker,context); // setup data to TemplateEngine
    }
}
