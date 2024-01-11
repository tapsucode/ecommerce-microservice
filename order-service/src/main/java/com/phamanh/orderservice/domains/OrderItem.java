package com.phamanh.orderservice.domains;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class OrderItem {

    @Id
    private Long id;

    private Long productId;

    private String titleProduct;

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
