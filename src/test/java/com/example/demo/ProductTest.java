package com.example.demo;


import com.example.demo.DAO.ProductRepository;
import com.example.demo.Obj.Product;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.Arrays;
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

    @Before
    public void init() {
        productRepository.deleteAll();

        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @After
    public void clear() {
        productRepository.deleteAll();
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

}
