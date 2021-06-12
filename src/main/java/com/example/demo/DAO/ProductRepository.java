package com.example.demo.DAO;

import com.example.demo.Obj.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends org.springframework.data.mongodb.repository.MongoRepository<Product, String> {
    List<Product> findByNameLike(String productName);

    List<Product> findById(List<String> ids);

    List<Product> findByNameLikeIgnoreCase(String name, Sort sort);

    @Query("{'$and': [{'price: {'$gte': ?0, '$lte': ?1}}, {'name':{'&regex': ?2, '$options': 'i'}}]}")
    List<Product> findByPriceBetweenAndNameLikeIgnoreCase(int priceFrom, int priceTo, String name, Sort sort);
//    boolean existsByEmail(String mail);

//    Optional<User> findByUsernameAndPassword
}
