package com.phamanh.saleservice.repository;

import com.phamanh.saleservice.domains.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {
    Warehouse findByAccountId();
}
