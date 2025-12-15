package com.koerber.orderservice.controller.dto;

import lombok.*;

@Getter
@Setter
public class InventoryRequest {
    private Long productId;
    private int quantity;
}
