package com.example.demo.converter;

import com.example.demo.Obj.Product;
import com.example.demo.ObjResponse.ProductResponse;
import com.example.demo.objRequest.ProductRequest;

public class ProductConverter {
    ProductConverter(){};

    public static Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }

    public static ProductResponse toProductResponse(Product request) {
        ProductResponse response = new ProductResponse();
        response.setId(request.getId());
        response.setPrice(request.getPrice());
        response.setName(request.getName());
        response.setCreator(request.getCreator());
        return response;
    }
}
