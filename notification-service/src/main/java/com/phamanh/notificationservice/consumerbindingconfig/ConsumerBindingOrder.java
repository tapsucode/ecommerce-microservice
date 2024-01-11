package com.phamanh.notificationservice.consumerbindingconfig;

import com.phamanh.notificationservice.request.Order;
import com.phamanh.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class ConsumerBindingOrder {

    private final OrderService orderService;

    @Bean
    public Consumer<Order> createOrderListener(){
        return msg -> {
            orderService.notificationOrder(msg);
        };
    }
}
