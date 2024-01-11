package com.phamanh.cartservice.service.implementation;

import com.phamanh.cartservice.converter.Product;
import com.phamanh.cartservice.domains.Cart;
import com.phamanh.cartservice.domains.CartItem;
import com.phamanh.cartservice.repository.CartItemRepository;
import com.phamanh.cartservice.repository.CartRepository;
import com.phamanh.cartservice.service.CartItemService;
import com.phamanh.commonservice.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImplementation implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;

    private  String URl_CHECK_PRODUCT_BY_ID = "http://localhost:8020/api/products/check/{productId}";


    @Override
    public CartItem createCartItem(CartItem cartItem) {

        Product product = checkProductExist(cartItem.getProductId());

        cartItem.setQuantity(cartItem.getQuantity());
        cartItem.setPrice(product.getPrice()*cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getDiscountedPrice()*cartItem.getQuantity());
        cartItem.setProductPrice(product.getPrice());
        cartItem.setProductDiscountedPrice(product.getDiscount());
        cartItem.setProductTitle(product.getTitle());
        cartItem.setProductImageUrl(product.getImageUrl());

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long accountId, Long id, CartItem cartItem) {

        CartItem item = findCartItemById(id);

        Cart cart = cartRepository.findByAccountId(cartItem.getCart().getId());

        if (cart.getAccountId() == accountId) {

            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getProductPrice());
            item.setDiscountedPrice(item.getProductDiscountedPrice() * item.getQuantity());

            return cartItemRepository.save(item);
        }
        throw new ResourceNotFoundException("Account not valid");
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Long productId, String size) {

        checkProductExist(productId);

        return cartItemRepository.isCartItemExit(cart,productId,size);
    }

    @Override
    public void removeCartItem(Long accountId, Long cartItemId) {

        CartItem cartItem = findCartItemById(cartItemId);

        Cart cart = cartRepository.findByAccountId(cartItem.getCart().getId());
        if(cart.getAccountId()==accountId){
            cartItemRepository.deleteById(cartItemId);
        }
        throw new ResourceNotFoundException("Account not valid");
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) {

        Optional<CartItem> otp = cartItemRepository.findById(cartItemId);

        if (otp.isPresent()) {
            return otp.get();
        }
        throw new ResourceNotFoundException("CartItem not found with id - "+cartItemId);
    }

    protected Product checkProductExist(Long productId){

        ResponseEntity<Product> productResponseEntity = restTemplate.getForEntity(URl_CHECK_PRODUCT_BY_ID, Product.class,productId);
        Product product = productResponseEntity.getBody();

        if (product == null) {
            throw new ResourceNotFoundException("Product not exists");
        }
        return product;
    }
}
