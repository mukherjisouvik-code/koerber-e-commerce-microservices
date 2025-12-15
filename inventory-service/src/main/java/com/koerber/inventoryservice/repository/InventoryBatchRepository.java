package com.koerber.inventoryservice.repository;

import com.koerber.inventoryservice.entity.InventoryBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryBatchRepository
        extends JpaRepository<InventoryBatch, Long> {

    List<InventoryBatch> findByProductIdOrderByExpiryDateAsc(Long productId);

    List<InventoryBatch> findByProductId(Long productId);
}
