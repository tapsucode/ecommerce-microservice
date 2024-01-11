package com.phamanh.cartservice.service;

import com.phamanh.cartservice.domains.Cart;
import com.phamanh.cartservice.request.AddItemRequest;

public interface CartService {

    Cart createCart(Long accountId);

    public String addCartItem(Long userId, AddItemRequest request);

    public Cart findUserCart(Long userId);
}
