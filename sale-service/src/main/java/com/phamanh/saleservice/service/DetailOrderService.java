package com.phamanh.saleservice.service;

public interface DetailOrderService {

    void confirmOrder(Long orderId,String orderCode);

    void cancelOrder(Long orderId,String orderCode) ;

    void confirmPaymentOrder(Long orderId,String orderCode);
}
