package com.example.demo.config;

import com.example.demo.filter.LogApiFilter;
import com.example.demo.filter.LogProcessFilterCustomer;
import com.example.demo.filter.LogProcessTimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean logProcessTimeFilter() {

        FilterRegistrationBean<LogProcessTimeFilter> bean = new FilterRegistrationBean();
        bean.setFilter(new LogProcessTimeFilter());
        bean.addUrlPatterns("/*");
        bean.setName("logProcessTimeFilter");
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public FilterRegistrationBean logApiFilter() {
        FilterRegistrationBean<LogApiFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LogApiFilter());
        bean.addUrlPatterns("/*");
        bean.setName("logApiFilter");
        bean.setOrder(0);
        return bean;
    }

//    @Bean
//    public FilterRegistrationBean logProcessFilterCustomer() {
//        FilterRegistrationBean<LogProcessFilterCustomer> bean = new FilterRegistrationBean<>();
//        bean.setFilter(new LogProcessFilterCustomer());
//        bean.addUrlPatterns("/customers/*");
//        bean.setName("logProcessFilterCustomer");
//        bean.setOrder(2);
//
//        return bean;
//    }


}
