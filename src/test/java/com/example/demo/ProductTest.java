package com.example.demo;


import com.example.demo.DAO.AppUserRepository;
import com.example.demo.DAO.ProductRepository;
import com.example.demo.Obj.Product;
import com.example.demo.Obj.app_user.AppUser;
import com.example.demo.Obj.app_user.UserAuthority;
import com.example.demo.objRequest.AuthRequest;
import org.json.JSONArray;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.xmlunit.util.Mapper;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {
    private HttpHeaders httpHeaders;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Before
    public void init() {
        productRepository.deleteAll();
        appUserRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @After
    public void clear() {

        productRepository.deleteAll();
        appUserRepository.deleteAll();
    }
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateProduct() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        JSONObject request = new JSONObject();
        request.put("name", "");
        request.put("price", -200);

        RequestBuilder requestBuilder =
                post("/products")
                    .headers(httpHeaders)
                    .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void testCreateProduct2() throws Exception {
//        config AppUser in database
        String password = "enon71214";
        AppUser appUser = new AppUser();
        appUser.setEmailAddress("none71214@gmail.com");
        appUser.setPassword(new BCryptPasswordEncoder().encode(password));
        appUser.setName("Ken");
        appUser.setAuthorities(Collections.singletonList(UserAuthority.ADMIN));
        appUserRepository.insert(appUser);



//        request post /auth api -> get the access token.
//        created AuthRequest
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(appUser.getEmailAddress());
        authRequest.setPassword(appUser.getPassword());

//        request body
        JSONObject JSONAuthRequest = new JSONObject();
        JSONAuthRequest.put("username", appUser.getEmailAddress());
        JSONAuthRequest.put("password", appUser.getPassword());

//        request api route -> request method, api route, request header, request body,
        RequestBuilder requestBuilder =
                post("/auth").headers(httpHeaders).content(JSONAuthRequest.toString());

        MvcResult result = mockMvc.perform(
                requestBuilder)
                .andExpect(status().isOk())
                .andReturn();


//        get the access token from response: JSON
        JSONObject tokenRes = new JSONObject(result.getResponse().getContentAsString());
        String accessToken = tokenRes.getString("token");


//        add access token into the httpHeaders key: AUTHORIZATION, value: Bearer accessToken
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        JSONObject productReq = new JSONObject();
        productReq.put("name", "Harry Potter");
        productReq.put("price", 450);

        mockMvc.perform(post("/products")
        .headers(httpHeaders)
        .content(productReq.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(productReq.getString("name")))
                .andExpect(jsonPath("$.price").value(productReq.getInt("price")))
                .andExpect(jsonPath("$.creator").value(appUser.getId()));



    }

    private Product createProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    @Test
    public void testGetProduct() throws Exception {
        Product product = createProduct("Economic", 400);
        productRepository.insert(product);

        mockMvc.perform(get("/products/" + product.getId())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));

    }

    @Test
    public void testReplaceProduct() throws Exception {
        Product product = createProduct("Economic", 400);
        productRepository.insert(product);

        JSONObject request = new JSONObject();
        request.put("name", "Macroeconomics");
        request.put("price", 666);


        mockMvc.perform(put("/products/" + product.getId())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));

    }

    @Test(expected = RuntimeException.class)
    public void testDeleteProduct() throws Exception {

        Product product = createProduct("Economics", 450);
        productRepository.insert(product);

        mockMvc.perform(delete("/products/" + product.getId())
                .headers(httpHeaders))
                .andExpect(status().isNoContent());
        productRepository.findById(product.getId())
                .orElseThrow(RuntimeException::new);
    }

    @Test
    public void testSearchProductsSortByPriceAsc() throws Exception {
        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Human Resource Management", 420);
        Product p4 = createProduct("Finance Management", 400);
        Product p5 = createProduct("Enterprise Resource Planning", 440);
        productRepository.insert(Arrays.asList(p1, p2, p3, p4, p5));

        MvcResult result = mockMvc.perform(get("/products")
            .headers(httpHeaders)
            .param("keyword", "Manage")
            .param("orderBy", "price")
            .param("sortRule", "asc"))
                .andReturn();
        MockHttpServletResponse mockHttpServletResponse = result.getResponse();
        String responseJSONStr = mockHttpServletResponse.getContentAsString();
        JSONArray productJSONArray = new JSONArray(responseJSONStr);

        List<String> productIds = new ArrayList();
        for (int i = 0; i < productJSONArray.length(); i++) {
            JSONObject productJSON = productJSONArray.getJSONObject(i);
            productIds.add(productJSON.getString("id"));
        }

        Assert.assertEquals(4, productIds.size());
        Assert.assertEquals(p2.getId(), productIds.get(0));
        Assert.assertEquals(p1.getId(), productIds.get(1));
        Assert.assertEquals(p4.getId(), productIds.get(2));
        Assert.assertEquals(p3.getId(), productIds.get(3));

        Assert.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());
        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, mockHttpServletResponse.getContentType());
//        mockMvc.perform(get("/products")
//                .headers(httpHeaders)
//                .param("keyword", "Manage")
//                .param("orderBy", "price")
//                .param("sortRule", "asc"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$[0].id").value(p2.getId()))
//                .andExpect(jsonPath("$[1].id").value(p1.getId()))
//                .andExpect(jsonPath("$[2].id").value(p4.getId()))
//                .andExpect(jsonPath("$[3].id").value(p3.getId()));



    }

    @Test
    public void get400WhenCreateProductWithEmptyName() throws Exception{
        JSONObject request = new JSONObject();
        request.put("name", "");
        request.put("price", 100);

        mockMvc.perform(
                post("/products").headers(httpHeaders).content(request.toString())
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenReplaceProductWithNagitivePrice() throws Exception {
        Product product = createProduct("Computer Science", 350);
        productRepository.insert(product);

        JSONObject request = new JSONObject();
        request.put("name", "Computer Science");
        request.put("price", -1);

        mockMvc.perform(
                put("/products/" + product.getId())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isBadRequest());
    }

}
