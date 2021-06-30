package com.example.demo.converter;

import com.example.demo.Obj.Customer;
import com.example.demo.objRequest.CustomerRequest;

public class CustomerConverter {

    public static Customer toCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setGender(request.getGender());
        customer.setSalary(request.getSalary());

        return customer;

    }
}
