package com.koerber.inventoryservice.service;

import com.koerber.inventoryservice.controller.dto.InventoryBatchResponse;
import com.koerber.inventoryservice.controller.dto.InventoryResponse;
import com.koerber.inventoryservice.entity.InventoryBatch;
import com.koerber.inventoryservice.enums.AllocationStrategyType;
import com.koerber.inventoryservice.factory.InventoryStrategyFactory;
import com.koerber.inventoryservice.repository.InventoryBatchRepository;
import com.koerber.inventoryservice.strategy.InventoryAllocationStrategy;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class InventoryService {

    private final InventoryBatchRepository repository;
    private final InventoryStrategyFactory factory;

    public InventoryService(InventoryBatchRepository repository,
                            InventoryStrategyFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public InventoryResponse getInventory(Long productId) {

        List<InventoryBatch> batches =
                repository.findByProductIdOrderByExpiryDateAsc(productId);

        if (batches.isEmpty()) {
            throw new IllegalArgumentException("No inventory found for product " + productId);
        }

        String productName = batches.get(0).getProductName();

        List<InventoryBatchResponse> batchResponses =
                batches.stream()
                        .map(b -> new InventoryBatchResponse(
                                b.getBatchId(),
                                b.getQuantity(),
                                b.getExpiryDate()
                        ))
                        .toList();

        return new InventoryResponse(
                productId,
                productName,
                batchResponses
        );
    }

    @Transactional
    public List<Long> reserveInventory(Long productId, int qty) {

        List<InventoryBatch> batches =
                repository.findByProductId(productId);

        InventoryAllocationStrategy strategy =
                factory.getStrategy(AllocationStrategyType.FEFO);

        List<InventoryBatch> sorted = strategy.sort(batches);
        List<Long> reserved = strategy.allocate(sorted, qty);

        repository.saveAll(sorted);
        return reserved;
    }

    public String getProductName(Long productId) {

        List<InventoryBatch> batches =
                repository.findByProductIdOrderByExpiryDateAsc(productId);

        if (batches.isEmpty()) {
            throw new IllegalArgumentException(
                    "No inventory found for productId " + productId
            );
        }

        return batches.get(0).getProductName();
    }
}
