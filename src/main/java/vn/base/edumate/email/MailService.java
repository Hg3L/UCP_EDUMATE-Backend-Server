package vn.base.edumate.email;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface MailService {

    void sendEmailWithTemplate(String recipient, String templateName, Map<String, Object> variables)
            throws MessagingException, UnsupportedEncodingException;
}
