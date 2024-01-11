package com.phamanh.cartservice.domains;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CartItem {

    @Id
    private Long id;

    private Long productId;

    private String productTitle;

    private String productImageUrl;

    private Integer productPrice;

    private Integer productDiscountedPrice;

    private int quantity;

    private String size;

    private Integer price;

    private Integer discountedPrice;

    private double subtotal;

    @JsonIgnore
    @ManyToOne
    private Cart cart;

}
