package com.example.demo.objRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class ProductRequest {

    @NotEmpty(message = "Product name is undefined.")
    private String name;

    @Min(value = 0, message = "Price should be positive or 0.")
    private int price;
    ProductRequest(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
