package com.purna.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductResponse {

    private String productName;
    private long productId;
    private long price;
    private long quantity;
}
