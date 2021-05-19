package com.example.demo.Service;

import com.example.demo.Exception.ConflictException;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.DAO.MockProductDAO;
import com.example.demo.Obj.Product;
import com.example.demo.Obj.ProductQueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private MockProductDAO productDAO;

    public Product createProduct(Product request) {
        boolean isIdDuplicated = productDAO.find(request.getId()).isPresent();

        if (isIdDuplicated) {
            throw new ConflictException("The id of the product is duplicate.");
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());


        return productDAO.insert(product);
    }

    public Product replaceProduct(String id, Product request) {
        Product product = getProduct(id);
        return productDAO.replace(product.getId(), request);
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productDAO.delete(product.getId());
    }

    public Product getProduct(String id) {
        return productDAO.find(id).orElseThrow(()-> new NotFoundException("Can't find product."));
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        return productDAO.find(param);
    }
}
