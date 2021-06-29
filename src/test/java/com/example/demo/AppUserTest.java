package com.example.demo;


import com.example.demo.DAO.AppUserRepository;
import com.example.demo.Obj.app_user.AppUser;
import com.example.demo.Obj.app_user.AppUserRequest;
import com.example.demo.Obj.app_user.UserAuthority;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    private HttpHeaders httpHeaders;
    private ObjectMapper mapper = new ObjectMapper();
    private final String URL_USER = "/users";

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @After
    public void clear() {
        appUserRepository.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception {
        AppUserRequest request = new AppUserRequest();
        request.setEmailAddress("admin@gmail.com");
        request.setPassword("123456");
        request.setName("Admin");
        request.setAuthorities(Arrays.asList(UserAuthority.ADMIN, UserAuthority.NORMAL));

        MvcResult result = mockMvc.perform(post(URL_USER)
        .headers(httpHeaders)
        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JSONObject responseBody = new JSONObject(result.getResponse().getContentAsString());
        String userId = responseBody.getString("id");

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(RuntimeException::new);
        Assert.assertEquals(request.getEmailAddress(), user.getEmailAddress());
        Assert.assertNotNull(user.getPassword());
        Assert.assertEquals(request.getName(), user.getName());
        Assert.assertArrayEquals(request.getAuthorities().toArray(), user.getAuthorities().toArray());


    }

}
