package com.phamanh.cartservice.controller;


import com.phamanh.cartservice.config.JwtProvider;
import com.phamanh.cartservice.domains.Cart;
import com.phamanh.cartservice.request.AddItemRequest;
import com.phamanh.cartservice.response.ApiResponse;
import com.phamanh.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")

@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @GetMapping("/")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) {

        Long accountId = JwtProvider.getAccountIdFromToken(jwt);
        Cart cart = cartService.findUserCart(accountId);

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest request, @RequestHeader("Authorization") String jwt) {

        Long accountId = JwtProvider.getAccountIdFromToken(jwt);
        cartService.addCartItem(accountId, request);

        ApiResponse apiResponse = new ApiResponse("Item add to cart",true);

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }


}
