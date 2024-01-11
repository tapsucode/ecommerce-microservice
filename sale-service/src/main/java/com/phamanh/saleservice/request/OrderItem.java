package com.phamanh.saleservice.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem {

    private Long orderItemId;

    private Long productId;

    private int quantity;

    private Long accountProduct;

}
