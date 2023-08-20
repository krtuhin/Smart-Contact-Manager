package com.smart.services;

import com.smart.constants.Constants;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    //method for sending email to user
    public boolean sendEmail(String receiver, String subject, String message) {

        boolean flag = false;

        //getting properties object
        Properties properties = System.getProperties();

        //setting email server properties
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);

        //getting mail session with sender authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.getUsername(), Constants.getPassword());
            }
        });

        //compose mail
        Message m = new MimeMessage(session);

        try {

            //setting mail properties
            m.setFrom(new InternetAddress(Constants.getSender()));
            m.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            m.setSubject(subject);
            //m.setText(message);
            m.setContent(message, "text/html");

            //sending email
            Transport.send(m);

            flag = true;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return flag;
    }
}
