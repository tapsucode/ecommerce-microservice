package com.phamanh.productreviews.domains;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class Rating {

    @Id
    private Long id;

    private Long productId;

    private int totalAccount;

    private byte totalRating;
}
