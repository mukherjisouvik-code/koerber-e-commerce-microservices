package com.koerber.inventoryservice.strategy.impl;

import com.koerber.inventoryservice.entity.InventoryBatch;
import com.koerber.inventoryservice.enums.AllocationStrategyType;
import com.koerber.inventoryservice.strategy.InventoryAllocationStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class FefoAllocationStrategy implements InventoryAllocationStrategy {

    @Override
    public AllocationStrategyType getType() {
        return AllocationStrategyType.FEFO;
    }

    @Override
    public List<InventoryBatch> sort(List<InventoryBatch> batches) {
        return batches.stream()
                .sorted(Comparator.comparing(InventoryBatch::getExpiryDate))
                .toList();
    }

    @Override
    public List<Long> allocate(List<InventoryBatch> batches, int requiredQty) {
        List<Long> reservedBatchIds = new ArrayList<>();

        for (InventoryBatch batch : batches) {
            if (requiredQty <= 0) break;

            int used = Math.min(batch.getQuantity(), requiredQty);
            batch.setQuantity(batch.getQuantity() - used);
            requiredQty -= used;

            reservedBatchIds.add(batch.getBatchId());
        }

        if (requiredQty > 0) {
            throw new IllegalStateException("Insufficient inventory");
        }

        return reservedBatchIds;
    }
}
