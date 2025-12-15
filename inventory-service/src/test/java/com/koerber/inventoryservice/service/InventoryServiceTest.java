package com.koerber.inventoryservice.service;

import com.koerber.inventoryservice.entity.InventoryBatch;
import com.koerber.inventoryservice.enums.AllocationStrategyType;
import com.koerber.inventoryservice.factory.InventoryStrategyFactory;
import com.koerber.inventoryservice.repository.InventoryBatchRepository;
import com.koerber.inventoryservice.strategy.InventoryAllocationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @Mock
    private InventoryBatchRepository repository;

    @Mock
    private InventoryStrategyFactory factory;

    @Mock
    private InventoryAllocationStrategy strategy;

    @InjectMocks
    private InventoryService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getInventory_shouldReturnInventoryResponse() {

        InventoryBatch batch = new InventoryBatch(
                1L, 1001L, "Laptop", 10, LocalDate.now().plusDays(10)
        );

        when(repository.findByProductIdOrderByExpiryDateAsc(1001L))
                .thenReturn(List.of(batch));

        var response = service.getInventory(1001L);

        assertEquals(1001L, response.productId());
        assertEquals("Laptop", response.productName());
        assertEquals(1, response.batches().size());
    }

    @Test
    void reserveInventory_shouldAllocateStock() {

        InventoryBatch batch = new InventoryBatch(
                1L, 1001L, "Laptop", 10, LocalDate.now().plusDays(10)
        );

        when(repository.findByProductId(1001L))
                .thenReturn(List.of(batch));

        when(factory.getStrategy(AllocationStrategyType.FEFO))
                .thenReturn(strategy);

        when(strategy.sort(any()))
                .thenReturn(List.of(batch));

        when(strategy.allocate(any(), eq(5)))
                .thenReturn(List.of(1L));

        List<Long> reserved =
                service.reserveInventory(1001L, 5);

        assertEquals(1, reserved.size());
        verify(repository).saveAll(any());
    }
}