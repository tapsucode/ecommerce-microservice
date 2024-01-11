package com.phamanh.saleservice.domains;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class Warehouse {

    @Id
    private Long id;

    @Column(unique = true)
    private Long accountId;

    private List<DetailOrder> detailOrders;

    private List<Inventory> inventories;

}
