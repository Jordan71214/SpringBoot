package com.example.demo.Controller;

import com.example.demo.Obj.Product;
import com.example.demo.Obj.ProductQueryParameter;
import com.example.demo.ObjResponse.ProductResponse;
import com.example.demo.Service.ProductService;
import com.example.demo.objRequest.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private ProductService productService;


//    private final List<Product> productDB = new ArrayList();
//
//    @PostConstruct
//    private void initDB(){
//
//        productService.createProduct(new Product("B0001", "Android Development (Java)", 380));
//        productService.createProduct(new Product("B0002", "Android Development (Kotlin)", 420));
//        productService.createProduct(new Product("B0003", "Data Structure (Java)", 250));
//        productService.createProduct(new Product("B0004", "Finance Management", 450));
//        productService.createProduct(new Product("B0005", "Human Resource Management", 330));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") String id){
        ProductResponse product = productService.getProductResponse(id);

        return ResponseEntity.ok(product);

//        Optional<Product> productOp = productDB.stream()
//                .filter(p -> p.getId().equals(id))
//                .findFirst();
//
//        if (!productOp.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        var product = productOp.get();
//        return ResponseEntity.ok().body(product);
    }


    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {

        ProductResponse product = productService.createProduct(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);

//        boolean isIdDuplicated = productDB.stream()
//                .anyMatch(p -> p.getId().equals(request.getId()));
//        if (isIdDuplicated) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//
//        Product product = new Product();
//        product.setId(request.getId());
//        product.setName(request.getName());
//        product.setPrice(request.getPrice());
//        productDB.add(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> replaceProduct(
            @PathVariable("id") String id,
            @Valid @RequestBody ProductRequest request){

        ProductResponse product = productService.replaceProduct(id, request);

//        Optional<Product> productOP = productDB.stream()
//                .filter(p -> p.getId().equals(id))
//                .findFirst();
//
//        if (!productOP.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Product product = productOP.get();
//        product.setName(request.getName());
//        product.setPrice(request.getPrice());

        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") String id) {

        productService.deleteProduct(id);

//        boolean isRemove = productDB.removeIf(p -> p.getId().equals(id));
//
//        if (isRemove) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }

        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/products")
//    public ResponseEntity<List<Product>> getProducts(
//            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
//        List<Product> products = productDB.stream()
//                .filter(p -> p.getName().toUpperCase().contains(keyword.toUpperCase()))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(products);
//    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            @ModelAttribute ProductQueryParameter param) {

        List<ProductResponse> products = productService.getProductResponses(param);
        return ResponseEntity.ok(products);


//        String nameKeyword = param.getKeyword();
//        String orderBy = param.getOrderBy();
//        String sortRule = param.getSortRule();
//
//        Comparator<Product> comparator = Objects.nonNull(orderBy) && Objects.nonNull(sortRule)
//                ? configureSortComparator(orderBy, sortRule)
//                : (p1, p2) -> 0;
//
//        List<Product> products = productDB.stream()
//                .filter(p -> p.getName().toUpperCase().contains(nameKeyword.toUpperCase()))
//                .sorted(comparator)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok().body(products);
    }

//    private Comparator<Product> configureSortComparator(String orderBy, String sortRule) {
//        Comparator<Product> comparator = (p1, p2) -> 0;
//
//        if (orderBy.equalsIgnoreCase("price")) {
//            comparator = Comparator.comparing(Product::getPrice);
//        } else if (orderBy.equalsIgnoreCase("name")) {
//            comparator = Comparator.comparing(Product::getName);
//        }
//
//        if (sortRule.equalsIgnoreCase("desc")) {
//            comparator = comparator.reversed();
//        }
//
//        return comparator;
//    }

}
