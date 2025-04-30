package vn.base.edumate.email;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import jakarta.mail.MessagingException;

public interface MailService {

    void sendEmailWithTemplate(String recipient, String templateName, Map<String, Object> variables)
            throws MessagingException, UnsupportedEncodingException;
}
