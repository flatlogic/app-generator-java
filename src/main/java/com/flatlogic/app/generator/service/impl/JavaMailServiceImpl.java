package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.exception.SendMailException;
import com.flatlogic.app.generator.service.JavaMailService;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * JavaMailService service.
 */
@Service
public class JavaMailServiceImpl implements JavaMailService {

    /**
     * String constant.
     */
    private static final String APPLICATION = "Application";

    /**
     * JavaMailSender instance.
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * MessageCodeUtil instance.
     */
    @Autowired
    private MessageCodeUtil messageCodeUtil;

    /**
     * Email from variable.
     */
    @Value("${email.from}")
    private String emailFrom;

    /**
     * Frontend host variable.
     */
    @Value("${frontend.host}")
    private String frontendHost;

    @Override
    public void sendEmailForCreateUser(final String email, final UUID token) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_AUTH_MAIL_VERIFY_EMAIL_SUBJECT, new Object[]{APPLICATION}));
            String resetUrl = UriComponentsBuilder.fromUriString(frontendHost).pathSegment("#/verify-email")
                    .queryParam(Constants.TOKEN, token.toString()).build().toUriString();
            helper.setText(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.MSG_AUTH_MAIL_VERIFY_EMAIL_BODY,
                    new Object[]{resetUrl, resetUrl, APPLICATION}), true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new SendMailException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_USER_EMAIL_VERIFICATION_RESET_OR_EXPIRED));
        }
    }

    @Override
    public void sendEmailForUpdateUserPasswordResetTokenAnd(final String email, final UUID token) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_AUTH_MAIL_RESET_PASSWORD_SUBJECT, new Object[]{APPLICATION}));
            String resetUrl = UriComponentsBuilder.fromUriString(frontendHost).pathSegment("#/password-reset")
                    .queryParam(Constants.TOKEN, token.toString()).build().toUriString();
            helper.setText(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.MSG_AUTH_MAIL_RESET_PASSWORD_BODY,
                    new Object[]{APPLICATION, email, resetUrl, resetUrl, APPLICATION}), true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new SendMailException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_USER_PASSWORD_RESET_SEND_MESSAGE));
        }
    }

}
