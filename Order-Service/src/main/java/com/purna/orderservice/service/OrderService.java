package com.purna.orderservice.service;

import com.purna.orderservice.model.OrderRequest;
import com.purna.orderservice.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
