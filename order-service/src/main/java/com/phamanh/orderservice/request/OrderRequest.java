package com.phamanh.orderservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private String orderCode;

    private List<OrderItemRequest> orderItemsResponse;
}
