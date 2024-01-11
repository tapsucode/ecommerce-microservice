package com.phamanh.orderservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse {

    private String message;

    private String paymentUrl;

    private Map<String, List<Long>> failDetails;
}
