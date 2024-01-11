package com.phamanh.orderservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    private Long orderItemId;

    private Long productId;

    private int quantity;

    private Long accountProduct;
}
