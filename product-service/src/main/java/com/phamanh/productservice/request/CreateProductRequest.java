package com.phamanh.productservice.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    private String title;

    private String description;

    private int price;

    private int discount;

    private int quantity;

    private String color;

    private String size;

    private String imageUrl;

    private String body;

    private String origin;

    private String brand;

    private byte warrantyDuration;

    private Set<Long> categoriesId;
}
