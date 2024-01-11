package com.phamanh.notificationservice.data;

import com.phamanh.notificationservice.request.OrderItem;
import com.phamanh.notificationservice.response.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileOrderItem {

    private double totalPay;

    private List<OrderItemResponse> orderItemResponse;
}
