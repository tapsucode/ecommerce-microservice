package com.phamanh.saleservice.controller;

import com.phamanh.saleservice.request.OrderRequest;
import com.phamanh.saleservice.response.OrderResponse;
import com.phamanh.saleservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("createOrder")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) throws Exception {

        OrderResponse orderResponse = inventoryService.createOrder(orderRequest);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);

    }
}
