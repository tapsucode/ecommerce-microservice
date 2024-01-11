package com.phamanh.saleservice.service;

import com.phamanh.saleservice.domains.Inventory;
import com.phamanh.saleservice.request.OrderRequest;
import com.phamanh.saleservice.response.OrderResponse;

public interface InventoryService {

    public Inventory test();

    OrderResponse createOrder(OrderRequest orderRequest) throws Exception ;


}
