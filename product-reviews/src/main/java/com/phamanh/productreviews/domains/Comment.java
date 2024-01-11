package com.phamanh.productreviews.domains;


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
public class Comment {

    @Id
    private Long id;

    private Long productId;

    private Long productAccountId;

    private Long accountId;

    private String username;

    private String body;

    private LocalDateTime createTime;
}
