//package com.purna.orderservice.service;
//
//import com.purna.orderservice.entity.Order;
//import com.purna.orderservice.exception.CustomException;
//import com.purna.orderservice.external.client.PaymentService;
//import com.purna.orderservice.external.client.ProductService;
//import com.purna.orderservice.external.request.PaymentRequest;
//import com.purna.orderservice.external.response.PaymentResponse;
//import com.purna.orderservice.model.OrderRequest;
//import com.purna.orderservice.model.OrderResponse;
//import com.purna.orderservice.model.PaymentMode;
//import com.purna.orderservice.model.ProductResponse;
//import com.purna.orderservice.repository.OrderRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.Instant;
//import java.util.Optional;
//
//@SpringBootTest
//public class OrderServiceImplTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private ProductService productService;
//
//    @Mock
//    private PaymentService paymentService;
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private OrderServiceImpl orderService;
//
//    @DisplayName("Get Order - Success Scenario")
//    @Test
//    void test_When_Order_Success(){
//        //Mocking
//        Order order = getMockOrder();
//        Mockito.when(orderRepository.findById(ArgumentMatchers.anyLong()))
//                .thenReturn(Optional.of(order));
//        Mockito.when(restTemplate.getForObject(
//                "http://PRODUCT-SERVICE/product/"+order.getProductId(),
//                ProductResponse.class
//        )).thenReturn(getMockProductResponse());
//        Mockito.when(restTemplate.getForObject(
//                "http://PAYMENT-SERVICE/payment/"+order.getId(),
//                PaymentResponse.class
//        )).thenReturn(getMockPaymentResponse());
//        //Actual
//        OrderResponse orderResponse = orderService.getOrderDetails(1);
//        //Verification
//        Mockito.verify(orderRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
//        Mockito.verify(restTemplate,Mockito.times(1)).getForObject(
//                "http://PRODUCT-SERVICE/product/"+order.getProductId(),
//                ProductResponse.class
//        );
//        Mockito.verify(restTemplate,Mockito.times(1)).getForObject(
//                "http://PAYMENT-SERVICE/payment/"+order.getId(),
//                PaymentResponse.class
//        );
//        //Assert
//        Assertions.assertNotNull(orderResponse);
//        Assertions.assertEquals(order.getId(),orderResponse.getOrderId());
//
//    }
//
//    @DisplayName("Get Order - Failed Scenario")
//    @Test
//    void test_When_Get_Order_NOT_FOUND_then_Not_Found(){
//            Mockito.when(orderRepository.findById(ArgumentMatchers.anyLong()))
//                    .thenReturn(Optional.ofNullable(null));
//
//        CustomException exception = Assertions.assertThrows(CustomException.class,
//                () -> orderService.getOrderDetails(1));
//        Assertions.assertEquals("NOT_FOUND",exception.getErrorCode());
//        Assertions.assertEquals(404,exception.getStatus());
//        Mockito.verify(orderRepository,Mockito.times(1)).findById(ArgumentMatchers.anyLong());
//
//    }
//
//    @Test
//    @DisplayName("Place Order - Success Scenario")
//    void test_When_Place_Order_Success(){
//        Order order = getMockOrder();
//        OrderRequest orderRequest = getMockOrderRequest();
//
//        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order);
//        Mockito.when(productService.reduceQuantity(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong()))
//                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
//        Mockito.when(paymentService.doPayment(ArgumentMatchers.any(PaymentRequest.class)))
//                .thenReturn(new ResponseEntity<Long>(1L,HttpStatus.OK));
//
//        long orderId = orderService.placeOrder(orderRequest);
//
//        Mockito.verify(orderRepository,Mockito.times(2))
//                .save(ArgumentMatchers.any());
//        Mockito.verify(productService, Mockito.times(1))
//                .reduceQuantity(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong());
//        Mockito.verify(paymentService,Mockito.times(1))
//                .doPayment(ArgumentMatchers.any(PaymentRequest.class));
//        Assertions.assertEquals(order.getId(),orderId);
//    }
//
//    @DisplayName("Placed Order - Payment Failed Scenario")
//    @Test
//    void test_when_Place_Order_Payment_Fails_then_Order_Placed(){
//        Order order = getMockOrder();
//        OrderRequest orderRequest = getMockOrderRequest();
//
//        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order);
//        Mockito.when(productService.reduceQuantity(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong()))
//                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
//        Mockito.when(paymentService.doPayment(ArgumentMatchers.any(PaymentRequest.class)))
//                .thenThrow(new RuntimeException());
//
//        long orderId = orderService.placeOrder(orderRequest);
//
//        Mockito.verify(orderRepository,Mockito.times(2))
//                .save(ArgumentMatchers.any());
//        Mockito.verify(productService, Mockito.times(1))
//                .reduceQuantity(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong());
//        Mockito.verify(paymentService,Mockito.times(1))
//                .doPayment(ArgumentMatchers.any(PaymentRequest.class));
//        Assertions.assertEquals(order.getId(),orderId);
//    }
//
//    private OrderRequest getMockOrderRequest() {
//        return OrderRequest.builder()
//                .productId(1)
//                .quantity(10)
//                .paymentMode(PaymentMode.CASH)
//                .totalAmount(100)
//                .build();
//    }
//
//    private PaymentResponse getMockPaymentResponse() {
//        return PaymentResponse.builder()
//                .paymentId(1)
//                .paymentDate(Instant.now())
//                .paymentMode(PaymentMode.CASH)
//                .amount(200)
//                .orderId(1)
//                .status("ACCEPTED")
//                .build();
//    }
//
//    private ProductResponse getMockProductResponse() {
//        return ProductResponse.builder()
//                .productId(2)
//                .productName("iPhone")
//                .price(100)
//                .quantity(200)
//                .build();
//    }
//
//    private Order getMockOrder() {
//        return Order.builder()
//                .orderStatus("Placed")
//                .orderDate(Instant.now())
//                .id(1)
//                .amount(100)
//                .quantity(200)
//                .productId(2)
//                .build();
//    }
//
//}