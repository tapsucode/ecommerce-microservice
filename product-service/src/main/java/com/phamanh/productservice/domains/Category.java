package com.phamanh.productservice.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class Category {

    @Id
    private Long id;

    private String categoryName;

    private LocalDateTime createDate;

    private Long accountId;

    @JsonIgnore
    private List<Product> products;
}
