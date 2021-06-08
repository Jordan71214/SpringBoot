package com.example.demo.DAO;

import com.example.demo.Obj.Product;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends org.springframework.data.mongodb.repository.MongoRepository<Product, String> {
    List<Product> findByNameLike(String productName);

    List<Product> findById(List<String> ids);

    List<Product> findByNameLikeIgnoreCase(String name, Sort sort);
//    boolean existsByEmail(String mail);

//    Optional<User> findByUsernameAndPassword
}
