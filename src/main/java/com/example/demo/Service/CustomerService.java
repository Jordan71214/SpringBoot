package com.example.demo.Service;

import com.example.demo.DAO.CustomerRepository;
import com.example.demo.DAO.MockCustomerDAO;
import com.example.demo.Exception.ConflictException;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Obj.Customer;
import com.example.demo.Obj.CustomerQueryParameter;
import com.example.demo.objRequest.CustomerConvert;
import com.example.demo.objRequest.CustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

//這遍用config檔去建立元件
//@Service
public class CustomerService {

//    @Autowired
//    private MockCustomerDAO customerDAO;

//  需要注入的部分, 在CustomerServiceConfig建立元件時, 使用param注入
//    @Autowired
    private CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }


    public Customer createCustomer(CustomerRequest request) {
//
//        boolean isIdDuplicated = customerDAO.find(request.getId()).isPresent();
//
//        if (isIdDuplicated) {
//            throw new ConflictException("The id of the customer us duplicated.");
//        }

        Customer customer = CustomerConvert.toCustomer(request);

//        return customerDAO.insert(customer);
        return repository.insert(customer);
    }

    public Customer replaceCustomer(String id, CustomerRequest request) {
        Customer oldCustomer = getCustomer(id);

        Customer customer = CustomerConvert.toCustomer(request);
        customer.setId(oldCustomer.getId());
//        return customerDAO.replace(customer.getId(), request);
        return repository.save(customer);
    }

    public void deleteCustomer(String id) {
        Customer customer = getCustomer(id);
//        customerDAO.delete(customer.getId());
        repository.deleteById(customer.getId());

    }


    public Customer getCustomer(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find Customer."));
//        return customerDAO.find(id)
//                .orElseThrow(() ->  new NotFoundException("Can't find Customer."));
    }

    public List<Customer> getCustomers(CustomerQueryParameter param) {
        String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();

        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)){
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = Sort.by(direction, orderBy);
        }
//        return customerDAO.find(param);
        return repository.findByNameLikeIgnoreCase(nameKeyword, sort);
    }
}
