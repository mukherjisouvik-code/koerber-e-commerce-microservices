package com.koerber.orderservice.controller.dto;

import java.util.List;

public record InventoryReserveResponse(
        Long productId,
        String productName,
        List<Long> reservedBatchIds
) {}
