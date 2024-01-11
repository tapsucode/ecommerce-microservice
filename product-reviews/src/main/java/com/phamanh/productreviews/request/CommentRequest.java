package com.phamanh.productreviews.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    private Long productId;

    private Long accountId;

    private String body;

}
