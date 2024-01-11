package com.phamanh.cartservice.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    private Long id;

    private String title;

    private String description;

    private Integer price;

    private String imageUrl;

    private int discount;

    private int quantity;

    private String color;

    private Long accountId;

}
