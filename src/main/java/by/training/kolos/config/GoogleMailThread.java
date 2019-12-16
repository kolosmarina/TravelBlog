package by.training.kolos.config;

import com.sun.mail.smtp.SMTPTransport;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class GoogleMailThread extends Thread {
    private static final Logger logger = LogManager.getLogger();

    private String username;
    private String password;
    private String title;
    private String recipientMail;
    private String message;

    public GoogleMailThread(String username, String password, String title, String recipientMail, String message) {
        this.username = username;
        this.password = password;
        this.title = title;
        this.recipientMail = recipientMail;
        this.message = message;
    }

    @Override
    public void run() {
        Properties sessionConfig = new Properties();
        // loading mail server parameters into mail session properties
        sessionConfig.setProperty("mail.smtps.host", "smtp.gmail.com");
        sessionConfig.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sessionConfig.setProperty("mail.smtp.socketFactory.fallback", "false");
        sessionConfig.setProperty("mail.smtp.socketFactory.port", "465");
        sessionConfig.setProperty("mail.smtp.port", "465");
        sessionConfig.setProperty("mail.smtps.auth", "true");
        sessionConfig.setProperty("mail.transport.protocol", "smtp");
        sessionConfig.setProperty("mail.smtp.quitwait", "false");

        Session session = Session.getInstance(sessionConfig);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientMail, false));
            mimeMessage.setSubject(title);
            mimeMessage.setText(message, "UTF-8");
            mimeMessage.setSentDate(new Date());

            try (SMTPTransport transport = (SMTPTransport) session.getTransport("smtps")) {
                transport.connect("smtp.gmail.com", username, password);
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            }
        } catch (MessagingException e) {
            logger.log(Level.ERROR, "Error with MimeMessage object", e);
        }
        logger.log(Level.DEBUG, "GoogleMailThread completed successfully");
    }
}
