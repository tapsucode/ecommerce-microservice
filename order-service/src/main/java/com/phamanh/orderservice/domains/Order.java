package com.phamanh.orderservice.domains;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Order {

    @Id
    private Long id;

    @Column(unique = true)
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

        CANCELLED, // da huy

        PAID // đã thanh toán
    }
}
