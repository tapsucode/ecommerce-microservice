package com.phamanh.notificationservice.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Order {

    private Long id;

    private String code;

    private Long accountId;

    private double totalPay;

    private LocalDateTime orderDate;

    private Payment payment;

    private Status status;

    private String note;

    private List<OrderItem> orderItems;

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
