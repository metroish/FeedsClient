package com.midcielab.utility;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.midcielab.model.Smtp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmtpUtility {

    private static SmtpUtility instance = new SmtpUtility();
    private static final Logger logger = LogManager.getLogger();

    private SmtpUtility() {
    }

    public static SmtpUtility getInstance() {
        return instance;
    }

    public boolean sendMail(Smtp smtp, String msgBody) {
        var prop = new Properties();
        prop.put("mail.smtp.host", smtp.getServer());
        prop.put("mail.smtp.port", smtp.getPort());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        var session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtp.getUser(), smtp.getPasswd());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtp.getUser()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(smtp.getMailto()));
            message.setSubject("[ " + TimeUtility.getInstance().getNow() + " ] Message from Feeds Client app");
            message.setText(msgBody);
            Transport.send(message);
        } catch (MessagingException e) {
            logger.error("Send mail fail", e);
            return false;
        }
        return true;
    }
}
