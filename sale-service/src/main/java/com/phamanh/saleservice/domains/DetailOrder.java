package com.phamanh.saleservice.domains;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class DetailOrder {

    @Id
    private Long id;

    private String orderCode;

    private Long productId;

    private Long orderId;

    private int quantity;

    private Warehouse warehouse;

    private Status status;

    public enum Status {
        PENDING,
        CANCEL,

        PLACED,

        PAID, // Đã thanh toán
    }
}
