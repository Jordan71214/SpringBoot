package com.example.demo;

import com.example.demo.DAO.CustomerRepository;
import com.example.demo.Obj.Customer;
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
public class CustomerTest {
    private HttpHeaders httpHeaders;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    CustomerRepository customerRepository;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        customerRepository.deleteAll();
    }

    @After
    public void clear() {
        customerRepository.deleteAll();
    }

    private Customer createCustomer(String name, String gender, int salary) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setGender(gender);
        customer.setSalary(salary);

        return customer;
    }

    @Test
    public void testCreateCustomer() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        JSONObject request = new JSONObject();
        request.put("name", "Ken");
        request.put("gender", "Male");
        request.put("salary", 10000);

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders
                    .post("/customers")//post request
                    .headers(httpHeaders)
                    .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.gender").value(request.getString("gender")))
                .andExpect(jsonPath("$.salary").value(request.getString("salary")))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));



    }

    @Test
    public void testGetCustomer() throws Exception {
        Customer customer = createCustomer("Ken", "Male", 10000);
        customerRepository.insert(customer);

        mockMvc.perform(
                get("/customers/" + customer.getId()).headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.gender").value(customer.getGender()))
                .andExpect(jsonPath("$.salary").value(customer.getSalary()));
    }

    @Test
    public void testReplaceCustomer() throws Exception {
        Customer customer = createCustomer("Ken", "Male", 10000);
        customerRepository.insert(customer);

        JSONObject request = new JSONObject();
        request.put("name", "Jordan");
        request.put("gender", "Female");
        request.put("salary", 20000);

        mockMvc.perform(
                put("/customers/" + customer.getId())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.gender").value(customer.getGender()))
                .andExpect(jsonPath("$.salary").value(customer.getSalary()));
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteCustomer() throws Exception {
        Customer customer = createCustomer("Ken", "Male", 10000);
        customerRepository.insert(customer);

        mockMvc.perform(
                delete("/customers/" + customer.getId())
                .headers(httpHeaders))
                .andExpect(status().isNoContent());

        customerRepository.findById(customer.getId())
                .orElseThrow(RuntimeException::new);

    }

    @Test
    public void testSearchCustomersSortByPriceAsc() throws Exception {
        Customer c1 = createCustomer("Ken", "Male", 1000);
        Customer c2 = createCustomer("Jordan", "Male", 3000);
        Customer c3 = createCustomer("Apple", "Female", 500);
        Customer c4 = createCustomer("Banana", "Male", 60);
        Customer c5 = createCustomer("Guava", "Male", 10000);
        customerRepository.insert(Arrays.asList(c1, c2, c3, c4, c5));


        /*
        mockMvc.perform(
                get("/customers")
                .headers(httpHeaders)
                .param("keyword", "a")
                .param("orderBy", "salary")
                .param("sortRule", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id").value(c4.getId()))
                .andExpect(jsonPath("$[1].id").value(c3.getId()))
                .andExpect(jsonPath("$[2].id").value(c2.getId()))
                .andExpect(jsonPath("$[3].id").value(c5.getId()));

         */

        MvcResult result = mockMvc.perform(
                get("/customers")
                        .headers(httpHeaders)
                        .param("keyword", "a")
                        .param("orderBy", "salary")
                        .param("sortRule", "asc"))
                        .andReturn();
        MockHttpServletResponse mockHttpServletResponse = result.getResponse();
        String responseJSONStr = mockHttpServletResponse.getContentAsString();
        JSONArray customerJSONArray = new JSONArray(responseJSONStr);

        List<String> customerIds = new ArrayList();
        for (int i = 0; i < customerJSONArray.length(); i++) {
            JSONObject customerJSON = customerJSONArray.getJSONObject(i);
            customerIds.add(customerJSON.getString("id"));
        };

        Assert.assertEquals(4, customerIds.size());
        Assert.assertEquals(c4.getId(), customerIds.get(0));
        Assert.assertEquals(c3.getId(), customerIds.get(1));
        Assert.assertEquals(c2.getId(), customerIds.get(2));
        Assert.assertEquals(c5.getId(), customerIds.get(3));

        Assert.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());
        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, mockHttpServletResponse.getContentType());
    }

    @Test
    public void get400WhenCreateCustomerWithEmptyName() throws Exception {

        JSONObject request = new JSONObject();
        request.put("name", "");
        request.put("gender", "Male");
        request.put("salary", 100);

        mockMvc.perform(
                post("/customers")
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isBadRequest());


    }

    @Test
    public void get400WhenReplaceCustomerWithNegativeSalary() throws Exception {
        Customer customer = createCustomer("HuangJianZhi", "Male", 100);
        customerRepository.insert(customer);

        JSONObject request = new JSONObject();
        request.put("name", "HuangJianZhi");
        request.put("gender", "Male");
        request.put("salary", -1);

        mockMvc.perform(
                put("/customers/" + customer.getId())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isBadRequest());
    }
}
