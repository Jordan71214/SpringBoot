package com.example.demo.Service;

import com.example.demo.DAO.MockCustomerDAO;
import com.example.demo.Exception.ConflictException;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Obj.Customer;
import com.example.demo.Obj.CustomerQueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private MockCustomerDAO customerDAO;

    public Customer createCustomer(Customer request) {
        boolean isIdDuplicated = customerDAO.find(request.getId()).isPresent();

        if (isIdDuplicated) {
            throw new ConflictException("The id of the customer us duplicated.");
        }

        Customer customer = new Customer();
        customer.setId(request.getId());
        customer.setName(request.getName());
        customer.setSalary(request.getSalary());
        customer.setGender(request.getGender());
        return customerDAO.insert(customer);
    }

    public Customer replaceCustomer(String id, Customer request) {
        Customer customer = getCustomer(id);
        return customerDAO.replace(customer.getId(), request);
    }

    public void deleteCustomer(String id) {
        Customer customer = getCustomer(id);
        customerDAO.delete(customer.getId());
    }

    public Customer getCustomer(String id) {

        return customerDAO.find(id)
                .orElseThrow(() ->  new NotFoundException("Can't find Customer."));
    }

    public List<Customer> getCustomers(CustomerQueryParameter param) {
        return customerDAO.find(param);
    }
}
