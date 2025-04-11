package com.purna.productservice.service;

import com.purna.productservice.model.ProductRequest;
import com.purna.productservice.model.ProductResponse;

public interface ProductService {

    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
