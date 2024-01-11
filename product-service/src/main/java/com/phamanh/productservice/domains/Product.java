package com.phamanh.productservice.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
@Builder
public class Product {

    @Id
    private Long id;

    private String title;

    private String description;

    private Integer price;

    private int discount;

    private int quantity;

    private String color;

    private String size;

    private String imageUrl;

    private String body;

    private String origin;

    private String brand;

    private byte warrantyDuration;

    private LocalDateTime createdDate;

    private Long accountId;


    private List<Category> categories;

}
