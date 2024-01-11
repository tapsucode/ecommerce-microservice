package com.phamanh.cartservice.repository;


import com.phamanh.cartservice.domains.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    public Cart findByUserId(Long userId);

    Cart findByAccountId(Long accountId);
}
