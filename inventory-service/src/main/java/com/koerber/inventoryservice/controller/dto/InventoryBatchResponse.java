package com.koerber.inventoryservice.controller.dto;

import java.time.LocalDate;

public record InventoryBatchResponse(
        Long batchId,
        int quantity,
        LocalDate expiryDate
) {}
