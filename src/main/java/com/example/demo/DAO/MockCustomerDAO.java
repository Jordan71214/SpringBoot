package com.example.demo.DAO;

import com.example.demo.Obj.Customer;
import com.example.demo.parameter.CustomerQueryParameter;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MockCustomerDAO {

    private List<Customer> customerDB = new ArrayList();

    public Customer insert(Customer customer) {
        customerDB.add(customer);
        return customer;
    }

    public Customer replace(String id, Customer customer) {
        Optional<Customer> customerOp = find(id);

        customerOp.ifPresent(c -> {
            c.setName(customer.getName());
            c.setGender(customer.getGender());
            c.setSalary(customer.getSalary());
        });

        return customer;
    }

    public void delete(String id) {
        customerDB.removeIf(c -> c.getId().equalsIgnoreCase(id));
    }

    public Optional<Customer> find(String id) {
        Optional<Customer> customerOp = customerDB.stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .findFirst();
        return customerOp;
    }

    public List<Customer> find(CustomerQueryParameter param) {
        String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();

        Comparator<Customer> comparator = Objects.nonNull(orderBy) && Objects.nonNull(sortRule)
                ? configureSortComparator(orderBy, sortRule)
                : (c1, c2) -> 0;
        return customerDB.stream()
                .filter(c -> c.getName().contains(nameKeyword))
                .collect(Collectors.toList());
    }

    private Comparator<Customer> configureSortComparator(String orderBy, String sortRule) {
        Comparator<Customer> comparator = (c1, c2) -> 0;

        if (orderBy.equalsIgnoreCase("name")) {
            comparator = Comparator.comparing(Customer::getName);
        } else if (orderBy.equalsIgnoreCase("salary")) {
            comparator = Comparator.comparing(Customer::getSalary);
        }

        if (sortRule.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
