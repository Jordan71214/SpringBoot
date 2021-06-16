package com.example.demo.config;

import com.example.demo.DAO.ProductRepository;
import com.example.demo.Service.MailService;
import com.example.demo.Service.ProductService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class ServiceConfig {

//    Bean表示該方法在spring啟動時執行, 參數會從ioC容器取得repo
//    參數就是告訴spring創建這個Bean需要哪些元件
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ProductService productService(ProductRepository repository, MailService mailService) {
//        return後, ioC容器就會取的return後的元件
        System.out.println("Create product service.");
        return new ProductService(repository, mailService);
    }


}
