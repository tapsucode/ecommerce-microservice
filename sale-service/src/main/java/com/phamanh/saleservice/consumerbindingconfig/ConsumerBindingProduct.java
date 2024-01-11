package com.phamanh.saleservice.consumerbindingconfig;

import com.phamanh.commonservice.kafkabridge.AccountVerification;
import com.phamanh.saleservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class ConsumerBindingProduct {

    private final InventoryService inventoryService;



    @Bean
    public Consumer<AccountVerification> createProductListener(){
        return msg ->
                inventoryService.test();
    }

    @Bean
    public Consumer<AccountVerification> updateProductListener(){
        return msg ->
                inventoryService.test();
    }
    @Bean
    public Consumer<AccountVerification> deleteProductListener(){
        return msg ->
                inventoryService.test();
    }
}
