package com.koerber.inventoryservice.strategy;

import com.koerber.inventoryservice.entity.InventoryBatch;
import com.koerber.inventoryservice.enums.AllocationStrategyType;

import java.util.List;

public interface InventoryAllocationStrategy {
    AllocationStrategyType getType();
    List<InventoryBatch> sort(List<InventoryBatch> batches);
    List<Long> allocate(List<InventoryBatch> batches, int qty);
}
