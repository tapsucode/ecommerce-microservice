package com.phamanh.productreviews.repository;

import com.phamanh.productreviews.domains.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {

    public Rating findByProductId(Long productId);
}
