package com.phamanh.saleservice.service.implementation;

import com.phamanh.saleservice.domains.DetailOrder;
import com.phamanh.saleservice.domains.Inventory;
import com.phamanh.saleservice.repository.DetailOrderRepository;
import com.phamanh.saleservice.repository.InventoryRepository;
import com.phamanh.saleservice.service.DetailOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DetailOrderServiceImplementation implements DetailOrderService {

    private final DetailOrderRepository detailOrderRepository;

    private final InventoryRepository inventoryRepository;


    @Override
    public void confirmOrder(Long orderId, String orderCode) {

        List<DetailOrder> detailOrder = detailOrderRepository.findAllByOrderCode(orderCode);

        detailOrder.forEach(item -> {item.setOrderId(orderId);
        item.setStatus(DetailOrder.Status.PLACED);
        });

        detailOrderRepository.saveAll(detailOrder);
    }

    @Transactional(rollbackOn = {Exception.class, Throwable.class})
    @Override
    public void cancelOrder(Long orderId, String orderCode) {
        List<DetailOrder> detailOrder = detailOrderRepository.findAllByOrderCode(orderCode);
        detailOrder.forEach(item -> {
            item.setStatus(DetailOrder.Status.CANCEL);

            Inventory byProductId = inventoryRepository.findByProductId(item.getProductId());
            byProductId.setQuantity(byProductId.getQuantity() + item.getQuantity());
            inventoryRepository.save(byProductId);
        });
        detailOrderRepository.saveAll(detailOrder);
    }

    @Override
    public void confirmPaymentOrder(Long orderId, String orderCode) {
        List<DetailOrder> detailOrder = detailOrderRepository.findAllByOrderCode(orderCode);

        detailOrder.forEach(item -> {item.setOrderId(orderId);
            item.setStatus(DetailOrder.Status.PAID);
        });

        detailOrderRepository.saveAll(detailOrder);
    }

}
