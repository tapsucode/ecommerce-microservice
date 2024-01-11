package com.phamanh.cartservice.service.implementation;

import com.phamanh.cartservice.domains.Cart;
import com.phamanh.cartservice.domains.CartItem;
import com.phamanh.cartservice.repository.CartRepository;
import com.phamanh.cartservice.request.AddItemRequest;
import com.phamanh.cartservice.service.CartItemService;
import com.phamanh.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    @Override
    public Cart createCart(Long accountId) {
        Cart cart = new Cart();
        cart.setId(accountId);
        return cartRepository.save(cart);
    }


    @Override
    public String addCartItem(Long userId, AddItemRequest request){

        Cart cart = cartRepository.findByUserId(userId);

        CartItem isPresent = cartItemService.isCartItemExist(cart,request.getProductId(),request.getSize());

        if (isPresent == null) {

            CartItem cartItem = new CartItem();
            cartItem.setProductId(request.getProductId());
            cartItem.setCart(cart);
            cartItem.setSize(request.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }
        return "Item Add To Cart";
    }

    @Override
    public Cart findUserCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId);

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem: cart.getCartItems()){
            totalPrice =totalPrice+ cartItem.getPrice();
            totalDiscountedPrice=totalDiscountedPrice+cartItem.getDiscountedPrice();
            totalItem =totalItem + cartItem.getQuantity();
        }

        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setDiscounte(totalPrice-totalDiscountedPrice);

        return cartRepository.save(cart);
    }
}
