package com.phamanh.orderservice.consumerbindingconfig;

import com.phamanh.orderservice.request.OrderTopic;
import com.phamanh.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
@RequiredArgsConstructor
public class ConsumerBindingPayment {

    private final OrderService orderService;

    @Bean
    public Consumer<OrderTopic> confirmPaymentOrderListener(){
        return msg ->
                orderService.confirmPaymentOrder(msg);
    }

    @Bean
    public Consumer<OrderTopic> failPaymentOrderListener(){
        return msg ->
                orderService.failPaymentOrder(msg);
    }
}
