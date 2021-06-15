package com.example.demo.Controller;

import com.example.demo.Service.MailService;
import com.example.demo.config.MailConfig;
import com.example.demo.objRequest.SendMailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/mail")
public class MailController {

    @Autowired
//    注入元件可標示名稱, 該元件生成時具有多種創建方法, 可以指定生成何種元件
//    @Qualifier(MailConfig.GMAIL_SERVICE)
    MailService mailService;

    @PostMapping
    public ResponseEntity<Void> sendMail(@Valid @RequestBody SendMailRequest request) {
        mailService.sendMail(request);
        return ResponseEntity.noContent().build();
    }
}
