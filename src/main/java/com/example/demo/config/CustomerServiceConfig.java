package com.example.demo.config;

import com.example.demo.DAO.CustomerRepository;
import com.example.demo.Service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerServiceConfig {

//    在spring啟動時呼叫
    @Bean
    public CustomerService customerService(CustomerRepository repository) throws Exception {
        return new CustomerService(repository);
    }


}
