package com.phamanh.cartservice.consumerbindingconfig;

import com.phamanh.cartservice.service.CartService;
import com.phamanh.commonservice.kafkabridge.AccountVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
@Configuration
@RequiredArgsConstructor
public class ConsumerBindingAccount {

    private final CartService cartService;
    @Bean
    public Consumer<Long> createCartListener(){
        return mgs->cartService.createCart(mgs);
    }
}
