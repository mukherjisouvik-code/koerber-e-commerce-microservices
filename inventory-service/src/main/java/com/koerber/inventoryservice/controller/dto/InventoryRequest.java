package com.koerber.inventoryservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

public record InventoryRequest(
        Long productId,
        int quantity
) {}
