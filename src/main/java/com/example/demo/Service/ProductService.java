package com.example.demo.Service;

import com.example.demo.DAO.ProductRepository;
import com.example.demo.Exception.ConflictException;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.DAO.MockProductDAO;
import com.example.demo.Obj.Product;
import com.example.demo.Obj.ProductQueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private MockProductDAO productDAO;
    @Autowired
    private ProductRepository repository;

    public Product createProduct(Product request) {


        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());


        return repository.insert(product);
    }

    public Product replaceProduct(String id, Product request) {
        Product oldProduct = getProduct(id);

        Product product = new Product();
        product.setId(oldProduct.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.save(product);
    }

    public void deleteProduct(String id) {

        repository.deleteById(id);
    }

    public Product getProduct(String id) {
        return repository.findById(id).orElseThrow(()-> new NotFoundException("Can't find product."));
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();

        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = Sort.by(direction, orderBy);
        }
        return repository.findByNameLikeIgnoreCase(nameKeyword, sort);
    }
}
