package vn.base.edumate.email;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Async
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSender implements MailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${system.default.email.from}")
    private String from;

    @Value("${system.default.email.name}")
    private String senderName;

    @Override
    public void sendEmailWithTemplate(String recipient, String templateName, Map<String, Object> variables)
            throws MessagingException, UnsupportedEncodingException {

        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process(templateName, context);
        String subject = (String) variables.getOrDefault("subject", "No Subject");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(from, senderName);
        mimeMessageHelper.setTo(recipient);
        mimeMessageHelper.setSubject(subject);

        mimeMessageHelper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
        log.info("Email sent to {} with subject {}", recipient, subject);
    }
}
