package com.example.demo.Obj;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Document(collection = "customer")
public class Customer {

    private String id;

    @NotEmpty(message = "Customer name is undefined.")
    private String name;

    private String gender;

    @Min(value = 0, message = "Customer salary should be positive or zero.")
    private int salary;

    public Customer(){};



    public Customer(String id, String name, String gender, int salary) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
