package com.phamanh.orderservice.service;

import com.phamanh.orderservice.domains.Address;
import com.phamanh.orderservice.domains.Order;
import com.phamanh.orderservice.domains.OrderItem;
import com.phamanh.orderservice.request.OrderTopic;
import com.phamanh.orderservice.response.CreateOrderResponse;


import java.util.List;

public interface OrderService {

    public CreateOrderResponse createOrder(String jwt, Address shippingAddress, List<OrderItem> orderItems, Order Order);

    public void confirmPaymentOrder(OrderTopic orderTopic);

    void failPaymentOrder(OrderTopic orderTopic);

//    public Order findOrderById(Long orderId) throws OrderException;
//
//    public List<Order> usersOrderHistory(Long userId);
//
//    public Order placedOrder(Long orderId) throws OrderException;
//
//    public Order confirmedOrder(Long orderId) throws OrderException;
//
//    public Order shippedOrder(Long orderId) throws OrderException;
//
//    public Order deliveredOrder(Long orderId) throws OrderException;
//
//    public Order canceledOrder(Long orderId) throws OrderException;
//
//    public List<Order> getAllOrders();
//
//    public void deleteOrder(Long orderId) throws OrderException;
}
