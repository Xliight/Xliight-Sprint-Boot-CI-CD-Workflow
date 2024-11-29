package com.ouitrips.app.services.messagingsystemservice.email;
import com.ouitrips.app.utils.VariableProperty;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService {
    private final FreeMarkerConfigurer freemarkerConfigurer;
    private final VariableProperty variableProperty;
    public void sendEmail(Map<String, Object> templateModel,
                                             String templateName,
                                             String toEMail,
                                             String subject) throws MessagingException, TemplateException, IOException {
        String fullName = (String)templateModel.get("fullName");
        Message message = createEmail(subject, getBodyTemplate(templateModel, templateName), toEMail, fullName);
        Transport.send(message);
    }
    public String getBodyTemplate(
            Map<String, Object> templateModel,
            String templateName) throws IOException, TemplateException {
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                .getTemplate("email/"+templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
    }
    private MimeMessage createEmail(String subject, String body, String toEMail, String fullName) throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = new MimeMessage(getEmailSession());
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        InternetAddress internetAddressFrom = new InternetAddress(variableProperty.getEmailFrom(), variableProperty.getEmailFromName());
        helper.setFrom(internetAddressFrom);
        InternetAddress internetAddressTo = new InternetAddress(toEMail, fullName);
        helper.setTo(internetAddressTo);
        helper.setSubject(subject);
        helper.setText(body, true);
        return mimeMessage;
    }
    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", variableProperty.getHost());
        properties.put("mail.smtp.port", variableProperty.getPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.user", variableProperty.getUsername());
        properties.put("mail.smtp.password", variableProperty.getPassword());
        return Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(variableProperty.getUsername(), variableProperty.getPassword());
                    }
                });
    }
}