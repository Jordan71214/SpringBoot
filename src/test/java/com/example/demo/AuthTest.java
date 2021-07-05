package com.example.demo;


import com.example.demo.DAO.AppUserRepository;
import com.example.demo.Obj.app_user.AppUser;
import com.example.demo.Obj.app_user.UserAuthority;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc


public class AuthTest {

    private HttpHeaders httpHeaders;

    @Autowired
    AppUserRepository appUserRepository;

    @Before
    public void init() {
        appUserRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @After
    public void clear() {
        appUserRepository.deleteAll();
    }

    @Test
    public void getTokenByUsernameAndPassword() throws Exception{
        String password = "password";
        AppUser appUser = new AppUser();
        appUser.setEmailAddress("account@gmail.com");
        appUser.setPassword(new BCryptPasswordEncoder().encode(password));
        appUser.setName("Ken");
        appUser.setAuthorities(Collections.singletonList(UserAuthority.ADMIN));
        appUserRepository.insert(appUser);
    }



}
