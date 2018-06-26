package com.oocl.trello.ticket.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

public class EmailBAFollowUpService {
    public EmailBAFollowUpService() {
    }

    public static void sendEmail(String emailBody, String recipients){

        Properties prop = new Properties();
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "2525");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
        prop.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("0e7db68b6f84d9","1f853c807463a6");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            System.out.println(recipients);
            InternetAddress[] parse = InternetAddress.parse(recipients , true);
            message.setRecipients(Message.RecipientType.TO,  parse);
            message.setSubject("Mail Subject");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(emailBody, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
