package com.example.demo;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MailTest {
    private HttpHeaders httpHeaders;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void sendMailTest() throws Exception {
        JSONObject request = new JSONObject();
        request.put("subject", "Java Mail Test");
        request.put("content", "本文將藉由寄送Email的程式, 來示範如何在Spring元件中注入這些設定值。");
        request.put("receivers", "none71214@gmail.com");

        RequestBuilder requestBuilder =
                post("/mail")
                        .headers(httpHeaders)
                        .content(request.toString());


    }
}
