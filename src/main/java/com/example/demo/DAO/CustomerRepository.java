package com.example.demo.DAO;

import com.example.demo.Obj.Customer;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByNameLikeIgnoreCase(String name, Sort sort);
}
