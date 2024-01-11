package com.phamanh.productservice.request;

import com.phamanh.productservice.domains.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {

    private String categoryName;

    private LocalDateTime createDate;

    private Long accountId;

}
