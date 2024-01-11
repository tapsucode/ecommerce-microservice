package com.phamanh.saleservice.consumerbindingconfig;

import com.phamanh.saleservice.request.OrderTopic;
import com.phamanh.saleservice.service.DetailOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class ConsumerBindingOrder {

    private final DetailOrderService detailOrderService;

    @Bean
    public Consumer<OrderTopic> confirmOrderListener(){
        return msg ->
                detailOrderService.confirmOrder(msg.getOrderId(), msg.getCode());
    }

    @Bean
    public Consumer<OrderTopic> cancelOrderListener(){
        return msg->detailOrderService.cancelOrder(msg.getOrderId(), msg.getCode());
    }

    @Bean
    public Consumer<OrderTopic> confirmPaymentOrderListener(){
        return msg->detailOrderService.confirmPaymentOrder(msg.getOrderId(), msg.getCode());
    }

}
