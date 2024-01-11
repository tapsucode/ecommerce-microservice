package com.phamanh.notificationservice.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long id;

    private ProductResponse productResponse;

    private Long accountProduct;

    private int quantity;

    private double subtotal;

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
