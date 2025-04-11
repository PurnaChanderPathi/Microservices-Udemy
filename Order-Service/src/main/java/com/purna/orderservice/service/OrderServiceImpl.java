package com.purna.orderservice.service;

import com.purna.orderservice.entity.Order;
import com.purna.orderservice.exception.CustomException;
import com.purna.orderservice.external.client.PaymentService;
import com.purna.orderservice.external.client.ProductService;
import com.purna.orderservice.external.request.PaymentRequest;
import com.purna.orderservice.external.response.PaymentResponse;
import com.purna.orderservice.model.OrderRequest;
import com.purna.orderservice.model.OrderResponse;
import com.purna.orderservice.model.ProductResponse;
import com.purna.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.slf4j.helpers.Util;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final PaymentService paymentService;

    private final RestTemplate restTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, PaymentService paymentService, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.paymentService = paymentService;
        this.restTemplate = restTemplate;
    }

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order Entity -> Save the order data with status Order Created
        //Product Entity - Block Products(Reduce the quantity)
        //payment service -> payments -> Success -> Complete, else Cancelled
        log.info("Placing order request : {}",orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        log.info("Creating Order with status CREATED");
        Order order = Order.builder()
                .id((long)0)
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("Calling payment service to complete the payment");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfully, Changing OrderStatus to Placed");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error occurred in Payment, Changing OrderStatus to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order placed successfully with orderId : {} ",order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get OrderDetails with OrderId : {}",orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order not found with orderId : "+orderId,"NOT_FOUND",404));
        log.info("Invoking Product service to fetch the product for id : {}",order.getProductId());

        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/"+order.getProductId(),
                    ProductResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .price(productResponse.getPrice())
                .quantity(productResponse.getQuantity())
                .build();

        log.info("Getting Payment Information from payment service with orderId : {}",order.getId());

        PaymentResponse paymentResponse = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/"+order.getId(),
                PaymentResponse.class);

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentMode(paymentResponse.getPaymentMode())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentStatus(paymentResponse.getStatus())
                .build();


        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
    }
}
