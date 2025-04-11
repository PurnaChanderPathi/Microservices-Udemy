package com.purna.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private long productId;
    private long totalAmount;
    private long quantity;
    private PaymentMode paymentMode;

//    public static void main(String[] args) {
//        HashMap<String, Integer> mp = new HashMap<>();
//        mp.put("a", 10);
//        mp.put("b", 40);
//        mp.put("c", 30);
//        mp.put("d", 20);
//        int[] max1 = {0}, max2 = {0};
//        String[] val = {""};
//
//        mp.entrySet().stream().forEach(a -> {
//            if (max1[0] < a.getValue()) {
//                max2[0] = max1[0];
//                max1[0] = a.getValue();
//            } else if (max2[0] < a.getValue()) {
//                max2[0] = a.getValue();
//                val[0] = a.getKey();
//            }
//        });
//        System.out.println(max2[0]+" "+val[0]);
//    }
}
