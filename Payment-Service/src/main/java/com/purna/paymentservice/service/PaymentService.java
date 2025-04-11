package com.purna.paymentservice.service;

import com.purna.paymentservice.model.PaymentRequest;
import com.purna.paymentservice.model.PaymentResponse;

public interface PaymentService {
    Long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(long orderId);
}
