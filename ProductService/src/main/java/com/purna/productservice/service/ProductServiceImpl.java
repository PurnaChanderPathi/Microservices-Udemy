package com.purna.productservice.service;

import com.purna.productservice.entity.Product;
import com.purna.productservice.exception.ProductServiceCustomException;
import com.purna.productservice.model.ProductRequest;
import com.purna.productservice.model.ProductResponse;
import com.purna.productservice.repository.ProductRespository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.*;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    private final ProductRespository productRespository;

    public ProductServiceImpl(ProductRespository productRespository) {
        this.productRespository = productRespository;
    }

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product..");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .productId((long)0).build();

        productRespository.save(product);
        log.info("Product Created : {}",product.getProductId());
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("get Product with productId : {}",productId);
       Product product = productRespository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product with given productId : "+productId+" not found","PRODUCT_NOT_FOUND"));
       ProductResponse productResponse = new ProductResponse();
        copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce quantity {} for Id: {}",quantity,productId);
        Product product = productRespository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product with given Id :"+productId+" not found","PRODUCT_NOT_FOUND"));
        if(product.getQuantity() < quantity){
            throw  new ProductServiceCustomException("Producr does not have sufficient Quantity","INSUFFICIENT_QUANTITY");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRespository.save(product);
        log.info("Product Quantity Updated Successfully...!");
    }
}
