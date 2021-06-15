package com.example.demo.config;

import com.example.demo.DAO.ProductRepository;
import com.example.demo.Service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

//    Bean表示該方法在spring啟動時執行
    @Bean
    public ProductService productService(ProductRepository repository) {
        return new ProductService(repository);
    }
}
