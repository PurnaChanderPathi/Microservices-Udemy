package com.purna.orderservice.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purna.orderservice.exception.CustomException;
import com.purna.orderservice.external.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(":: URL - {}", response.request().url());
        log.info(":: Headers - {}", response.request().headers());

        try {
            if (response.body() != null) {
                ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
                return new CustomException(
                        errorResponse.getErrorMessage(),
                        errorResponse.getErrorCode(),
                        response.status()
                );
            } else {
                log.warn("Feign error response body is null. Status: {}", response.status());
                return new CustomException(
                        "Empty error response from service",
                        "NO_RESPONSE_BODY",
                        response.status()
                );
            }
        } catch (IOException e) {
            log.error("Error decoding error response: {}", e.getMessage(), e);
            return new CustomException("Internal Server Error", "INTERNAL_SERVER_ERROR", 500);
        }
    }
}

