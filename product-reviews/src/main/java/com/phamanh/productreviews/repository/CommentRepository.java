package com.phamanh.productreviews.repository;

import com.phamanh.productreviews.domains.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    public Comment findByProductId(Long productId);

}
