package com.phamanh.saleservice.service.implementation;

import com.phamanh.saleservice.domains.DetailOrder;
import com.phamanh.saleservice.domains.Inventory;
import com.phamanh.saleservice.domains.Warehouse;
import com.phamanh.saleservice.repository.DetailOrderRepository;
import com.phamanh.saleservice.repository.InventoryRepository;
import com.phamanh.saleservice.repository.WarehouseRepository;
import com.phamanh.saleservice.request.OrderItem;
import com.phamanh.saleservice.request.OrderRequest;
import com.phamanh.saleservice.response.OrderResponse;
import com.phamanh.saleservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryServiceImplementation implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final DetailOrderRepository detailOrderRepository;

    private final WarehouseRepository warehouseRepository;

    @Override
    public Inventory test() {
        return null;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) throws Exception {

        OrderResponse orderResponse = new OrderResponse();

        String message = "Success";

        Map<String,List<Long>> responseId = new HashMap<>();

        List<Long> invalidIds = new ArrayList<>();
        List<Long> failIds = new ArrayList<>();

        responseId.put("Invalid",invalidIds);
        responseId.put("Fail",failIds);


        for (OrderItem orderItem : orderRequest.getOrderItems()) {

            Inventory inventory = inventoryRepository.findByProductId(orderItem.getProductId());

            if (inventory == null || inventory.getStatus().equals(Inventory.Status.REMOVED) ) {

                List<Long> invalid = responseId.get("Invalid");
                invalid.add(orderItem.getProductId());
                responseId.put("Invalid",invalid);

                message = "Fail";

            } else if (inventory.getQuantity()< orderItem.getQuantity()) {
                List<Long> failId = responseId.get("fail");
                failId.add(orderItem.getProductId());
                responseId.put("fail",failId);
                message = "Fail";
            }
        }

        orderResponse.setResults(responseId);

        if (message.equals("Success")){
            createOrderDetails(orderRequest);
            orderResponse.setMessage("Success");

        } else {
            orderResponse.setMessage("Fail");
        }

        return orderResponse;
    }


    @Transactional(rollbackOn = {Exception.class, Throwable.class})
    protected void createOrderDetails(OrderRequest orderRequest) throws Exception{

        for (OrderItem orderItem: orderRequest.getOrderItems()) {

            Inventory byProductId = inventoryRepository.findByProductId(orderItem.getProductId());

            Warehouse byAccountId = warehouseRepository.findByAccountId();

            DetailOrder detailOrder = new DetailOrder();
            detailOrder.setOrderCode(orderRequest.getOrderCode());
            detailOrder.setProductId(orderItem.getProductId());
            detailOrder.setQuantity(orderItem.getQuantity());
            detailOrder.setStatus(DetailOrder.Status.PENDING);
            detailOrder.setWarehouse(byAccountId);

            byProductId.setQuantity(byProductId.getQuantity()-orderItem.getQuantity());

            detailOrderRepository.save(detailOrder);
            inventoryRepository.save(byProductId);

            throw new Exception();
        }

    }
}
