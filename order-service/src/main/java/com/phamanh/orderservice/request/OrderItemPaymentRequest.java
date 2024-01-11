package com.phamanh.orderservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemPaymentRequest {

    private String titleProduct;

    private int quantity;

    private double subtotal;
}
