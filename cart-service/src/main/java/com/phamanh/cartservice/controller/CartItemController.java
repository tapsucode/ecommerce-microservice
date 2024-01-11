package com.phamanh.cartservice.controller;

import com.phamanh.cartservice.config.JwtProvider;
import com.phamanh.cartservice.domains.CartItem;
import com.phamanh.cartservice.response.ApiResponse;
import com.phamanh.cartservice.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cartItem")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PutMapping("/updateItem")
    public ResponseEntity<?> updateCartItem(@RequestBody CartItem cartItem, @RequestParam("accountId") Long accountId,
                                            @RequestHeader("Authorization") String jwt) {

        Long accountIdByJwt = JwtProvider.getAccountIdFromToken(jwt);

        CartItem cartItemUpdate = cartItemService.updateCartItem(accountIdByJwt,cartItem.getId(), cartItem);

        return new ResponseEntity<>(cartItemUpdate, HttpStatus.OK);

    }
    @DeleteMapping("/removeItem/{cartItemId}")
    public ResponseEntity< ? > removeCartItem(@PathVariable("cartItemId") Long cartItemId,@RequestHeader("Authorization") String jwt){

        Long accountIdByJwt = JwtProvider.getAccountIdFromToken(jwt);
        cartItemService.removeCartItem(accountIdByJwt,cartItemId);

        return new ResponseEntity<>(new ApiResponse("Item has been removed",true),HttpStatus.OK);
    }

}
