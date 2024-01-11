package com.phamanh.saleservice.domains;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class Inventory {

    @Id
    private Long id;

    @Column(unique = true)
    private Long productId;

    private int quantity;

    private Warehouse warehouse;

    private Status status;

    public enum Status {
        ACTIVE,

        REMOVED
    }

}
