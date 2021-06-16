package com.example.demo.objRequest;

import com.example.demo.Obj.Product;
import com.example.demo.ObjResponse.ProductResponse;

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

        return response;
    }
}
