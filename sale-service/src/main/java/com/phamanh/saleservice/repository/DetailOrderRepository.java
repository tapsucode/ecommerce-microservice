package com.phamanh.saleservice.repository;

import com.phamanh.saleservice.domains.DetailOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetailOrderRepository extends JpaRepository<DetailOrder,Long> {
    List<DetailOrder> findAllByOrderCode(String orderCode);
}
