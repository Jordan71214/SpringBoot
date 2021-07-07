package com.example.demo.Service;

import com.example.demo.DAO.ProductRepository;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Obj.Product;
import com.example.demo.aop.ActionType;
import com.example.demo.aop.EntityType;
import com.example.demo.aop.SendEmail;
import com.example.demo.auth.UserIdentity;
import com.example.demo.parameter.ProductQueryParameter;
import com.example.demo.ObjResponse.ProductResponse;
import com.example.demo.converter.ProductConverter;
import com.example.demo.objRequest.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

//@Service
public class ProductService {

//    @Autowired
//    private MockProductDAO productDAO;

//    @Autowired
    private ProductRepository repository;
    private MailService mailService;

    @Autowired
    private UserIdentity userIdentity;

    public ProductService(ProductRepository repository, MailService mailService) {
        this.repository = repository;
        this.mailService = mailService;
    }




    @SendEmail(entity = EntityType.PRODUCT, action = ActionType.CREATE)
    public ProductResponse createProduct(ProductRequest request) {

        Product product = ProductConverter.toProduct(request);
        product.setCreator(userIdentity.getId());
        product = repository.insert(product);

//        mailService.sendNewProductMail(product.getId());
        return ProductConverter.toProductResponse(product);
    }

    @SendEmail(entity = EntityType.PRODUCT, action = ActionType.UPDATE)
    public ProductResponse replaceProduct(String id, ProductRequest request) {
        Product oldProduct = getProduct(id);

        Product product = ProductConverter.toProduct(request);
        product.setId(oldProduct.getId());
        product.setCreator(oldProduct.getCreator());

        repository.save(product);

        return ProductConverter.toProductResponse(product);
    }

    @SendEmail(entity = EntityType.PRODUCT, action = ActionType.DELETE)
    public void deleteProduct(String id) {
//        mailService.sendDeleteProductMail(id);

        repository.deleteById(id);
    }

    public ProductResponse getProductResponse(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
        return ProductConverter.toProductResponse(product);
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



    public List<ProductResponse> getProductResponses(ProductQueryParameter param) {
        String nameKeyWord = Optional.ofNullable(param.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(param.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(param.getPriceTo()).orElse(Integer.MAX_VALUE);
        Sort sort = configureSort(param.getOrderBy(), param.getSortRule());

        List<Product> products = repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, nameKeyWord, sort);

        return products.stream()
                .map(ProductConverter::toProductResponse)
                .collect(Collectors.toList());
    }

    private Sort configureSort(String orderBy, String sortRule) {
        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = Sort.by(direction, orderBy);

        }

        return sort;
    }



}
