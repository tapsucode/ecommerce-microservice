package com.phamanh.cartservice.service;

import com.phamanh.cartservice.domains.Cart;
import com.phamanh.cartservice.domains.CartItem;

public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItem(Long accountId,Long id, CartItem cartItem);

    public CartItem isCartItemExist(Cart cart, Long productId, String size);

    public void removeCartItem(Long accountId,Long cartItemId);

    public CartItem findCartItemById(Long cartItemId);
}
