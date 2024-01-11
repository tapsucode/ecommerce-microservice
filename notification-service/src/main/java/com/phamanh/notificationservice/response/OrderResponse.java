package com.phamanh.notificationservice.response;

import com.phamanh.notificationservice.request.Address;
import com.phamanh.notificationservice.request.Order;
import com.phamanh.notificationservice.request.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private String code;

    private String userName;

    private String email;

    private double totalPay;

    private LocalDateTime orderDate;

    private Order.Payment payment;

    private Order.Status status;

    private String note;

    private List<OrderItemResponse> orderItemResponses;

    private Address address;

    public enum Payment{
        CASH, // tien mat
        CARD, // the
        COD
    }
    public enum Status{
        PROCESSING, // dang xu li
        SHIPPED, // da gui
        COMPLETED,// hoan thanh

        PLACED, // dat hang

        CONFIRMED, // da xax nhan

        DELIVERED, // da giao hang

        CANCELLED // da huy
    }
}
