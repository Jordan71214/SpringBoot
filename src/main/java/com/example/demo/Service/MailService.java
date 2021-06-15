package com.example.demo.Service;

import com.example.demo.config.MailConfig;
import com.example.demo.objRequest.SendMailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MailService {

//在建立元件時, 初始化元件需要的資料

    private Properties props;
    private InternetAddress fromAddress;
    private Authenticator authenticator;
//元件建立時一併接收資料
    public MailService(Properties props, InternetAddress fromAddress, Authenticator authenticator) {
        this.props = props;
        this.fromAddress = fromAddress;
        this.authenticator = authenticator;
    }

    public void sendMail(SendMailRequest request)  {

//        final String host = "smtp.gmail.com";
//        final int port = 587;
//        final boolean enableAuth = true;
//        final boolean enableStarttls = true;
//        final String userAddress = "none71214@gmail.com";
//        final String pwd = "enon71214";
//        final String userDisplayName = "Junior Software Engineer";

//        Properties props = new Properties();
//        props.put("mail.smtp.host", mailConfig.getHost());
//        props.put("mail.smtp.port", mailConfig.getPort());
//        props.put("mail.smtp.auth", String.valueOf(mailConfig.isAuthEnable()));
//        props.put("mail.smtp.starttls.enable", String.valueOf(mailConfig.isStarttlsEnabled()));

        Session session = Session.getInstance(props, authenticator);

        try {
            Message message = new MimeMessage(session);
            message.setSubject(request.getSubject());
            message.setContent(request.getContent(), "text/html; charset=UTF-8");
            message.setFrom(fromAddress);
            for (String address : request.getReceivers()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            }

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }













}
