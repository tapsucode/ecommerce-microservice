package com.phamanh.cartservice.repository;

import com.phamanh.cartservice.domains.Cart;
import com.phamanh.cartservice.domains.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query("SELECT ci From CartItem ci Where ci.cart=:cart AND ci.productId=:productId AND ci.size=:size")
    public CartItem isCartItemExit(@Param("cart") Cart cart, @Param("productId")Long productId, @Param("size")String size);
}
