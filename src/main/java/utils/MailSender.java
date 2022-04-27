package utils;

import java.util.Properties;
import java.util.Random;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

    private String from;
    private String host;
    private Properties properties;
    private Session session;

    /**
     * Constructor. Setups the mail server.
     */
    public MailSender() {
        from = "betandruin22@gmail.com";
        host = "smtp.gmail.com";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("betandruin22@gmail.com", "ruinandbet22");
            }
        });

        // Used to debug SMTP issues
        // session.setDebug(true);
    }

    /**
     * This method will send a password reset email to the given email.
     * @param destination user's email
     * @return the code to change the password
     */
    public void sendPasswordResetEmail(String username, String destination, String code) throws MessagingException {
        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));

        // Set Subject: header field
        message.setSubject("Password change authorization");

        MimeMultipart multipart = new MimeMultipart("related");

        // First part
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<div style=\"margin: 0 auto; width: 500px; padding: 20px; background-color: #ffffff; border: 1px solid #EBEDEF; font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif\">\n" +
                "        <h1 style=\"font-weight: 100;\">Hello " + username + ",</h1>\n" +
                "        <p style=\"font-weight: bold;\">A request has been received to change the password for your BetAndRuin account.</p>\n" +
                "        <p>You code is: <b>" + code + "</b>.</p>\n" +
                "        <p>If you did not initiate this request, please contact us immediately at betandruin22@gmail.com</p>\n" +
                "        <br>\n" +
                "        <p>Thank you,</p>\n" +
                "        <p>Josefinators team, from BetAndRuin</p>\n" +
                "    </div>\n" +
                "    \n" +
                "    <div style=\"text-align: center;\">\n" +
                "        <img width=\"150px\" src=\"cid:image\" alt=\"logo\">\n" +
                "    </div>";
        messageBodyPart.setContent(htmlText, "text/html");
        multipart.addBodyPart(messageBodyPart);

        // Second part
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource("src/main/resources/img/final_logo.png");
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<image>");
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        System.out.println("sending...");
        // Send message
        Transport.send(message);
        System.out.println("email sent successfully!");
    }
}