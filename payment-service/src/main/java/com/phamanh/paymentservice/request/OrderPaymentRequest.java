package com.phamanh.paymentservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentRequest {

    private Long id;

    private String code;

    private Long accountId;

    private double totalPay;

    private List<OrderItemPaymentRequest> orderItemPaymentRequests;
}
