package com.example.demo.objRequest;

import com.example.demo.Obj.Customer;

public class CustomerConvert {

    public static Customer toCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setGender(request.getGender());
        customer.setSalary(request.getSalary());

        return customer;

    }
}
