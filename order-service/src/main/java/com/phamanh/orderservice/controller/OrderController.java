package com.phamanh.orderservice.controller;


import com.phamanh.commonservice.exceptions.UserException;
import com.phamanh.orderservice.domains.Address;
import com.phamanh.orderservice.domains.Order;
import com.phamanh.orderservice.domains.OrderItem;
import com.phamanh.orderservice.response.CreateOrderResponse;
import com.phamanh.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt,
                                                           @RequestBody List<OrderItem> orderItems, @RequestBody Order order) {

        CreateOrderResponse createOrderResponse = orderService.createOrder(jwt,shippingAddress,orderItems,order);

        return new ResponseEntity<>(createOrderResponse, HttpStatus.CREATED);

    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException{

        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());

        return new ResponseEntity<>(orders,HttpStatus.CREATED);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Order> findOrderById(@PathVariable("Id")Long id,@RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.findOrderById(id);

        if (user.getId().equals(order.getUser().getId())){
            return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
        }

        throw new UserException("User not valid");
    }
}
