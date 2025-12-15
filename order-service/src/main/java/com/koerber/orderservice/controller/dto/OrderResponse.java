package com.koerber.orderservice.controller.dto;

import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        Long orderId,
        Long productId,
        String productName,
        int quantity,
        String status,
        LocalDate orderDate,
        List<Long> reservedBatchIds
) {}
