package com.example.demo.Service;

import com.example.demo.DAO.CustomerRepository;
import com.example.demo.DAO.MockCustomerDAO;
import com.example.demo.Exception.ConflictException;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Obj.Customer;
import com.example.demo.Obj.CustomerQueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private MockCustomerDAO customerDAO;
    @Autowired
    private CustomerRepository repository;

    public Customer createCustomer(Customer request) {
//
//        boolean isIdDuplicated = customerDAO.find(request.getId()).isPresent();
//
//        if (isIdDuplicated) {
//            throw new ConflictException("The id of the customer us duplicated.");
//        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setSalary(request.getSalary());
        customer.setGender(request.getGender());
//        return customerDAO.insert(customer);
        return repository.insert(customer);
    }

    public Customer replaceCustomer(String id, Customer request) {
        Customer oldcustomer = getCustomer(id);

        Customer customer = new Customer();
        customer.setId(oldcustomer.getId());
        customer.setGender(oldcustomer.getGender());
        customer.setName(oldcustomer.getName());
        customer.setSalary(oldcustomer.getSalary());
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
