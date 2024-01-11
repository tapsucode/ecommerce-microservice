package com.phamanh.notificationservice.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem {

    private Long id;

    private Long productId;

    private Long accountProduct;

    private int quantity;

    private double subtotal;

    @JsonIgnore
    private Order order;

    private Enum Status;

    public enum Status{
        PROCESSING, // dang xu li
        SHIPPED, // da gui
        COMPLETED,// hoan thanh

        CONFIRMED, // da xax nhan

        DELIVERED, // da giao hang

        CANCELLED, // da huy

        PLACED
    }

}
