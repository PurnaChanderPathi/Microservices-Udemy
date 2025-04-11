package com.purna.paymentservice.service;

import com.purna.paymentservice.entity.TransactionDetails;
import com.purna.paymentservice.model.PaymentMode;
import com.purna.paymentservice.model.PaymentRequest;
import com.purna.paymentservice.model.PaymentResponse;
import com.purna.paymentservice.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService{

    private final TransactionDetailsRepository transactionDetailsRepository;

    public PaymentServiceImpl(TransactionDetailsRepository transactionDetailsRepository) {
        this.transactionDetailsRepository = transactionDetailsRepository;
    }

    @Override
    public Long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording PaymentDetails : {}",paymentRequest);
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .id((long)0)
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("Success")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .paymentDate(Instant.now())
                .build();
        transactionDetailsRepository.save(transactionDetails);
        log.info("Transaction Completed with Id : {}",transactionDetails.getId());
        return transactionDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {
        log.info("getting payment details for the orderId : {}",orderId);
        TransactionDetails transactionDetails = transactionDetailsRepository.findByOrderId(orderId);
        return PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .amount(transactionDetails.getAmount())
                .orderId(transactionDetails.getOrderId())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .paymentDate(transactionDetails.getPaymentDate())
                .status(transactionDetails.getPaymentStatus())
                .build();
    }
}
