package com.example.demo.Controller;

import com.example.demo.Obj.Customer;
import com.example.demo.Obj.CustomerQueryParameter;
import com.example.demo.Obj.Product;
import com.example.demo.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    private final List<Customer> customerDB = new ArrayList();

    @PostConstruct
    private void initDB() {
        customerDB.add(new Customer("C001", "Ken", "M", 10000));
        customerDB.add(new Customer("C002", "Jordan", "M", 12000));
        customerDB.add(new Customer("C003", "Apple", "M", 11000));
        customerDB.add(new Customer("C004", "Banana", "M", 13000));
        customerDB.add(new Customer("C005", "Banana Test", "M", 10500));
    }

//    @GetMapping("/{id}")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") String id) {
        Customer customer = customerService.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

//    @PostMapping
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer request) {
        Customer customer = customerService.createCustomer(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customer.getId())
                .toUri();
        return ResponseEntity.created(location).body(customer);
    }

//    @PutMapping("/{id}")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Customer> replaceCustomer(
            @PathVariable("id") String id, @RequestBody Customer request) {

        Customer customer = customerService.replaceCustomer(id, request);
        return ResponseEntity.ok().body(customer);
    }

//    @DeleteMapping("/{id}")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> getCustomers(
            @ModelAttribute CustomerQueryParameter param) {
        List<Customer> customers = customerService.getCustomers(param);

        return ResponseEntity.ok().body(customers);
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
