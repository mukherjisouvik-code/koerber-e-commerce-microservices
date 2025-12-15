package com.koerber.inventoryservice.controller;

import com.koerber.inventoryservice.controller.dto.InventoryRequest;
import com.koerber.inventoryservice.controller.dto.InventoryReserveResponse;
import com.koerber.inventoryservice.controller.dto.InventoryResponse;
import com.koerber.inventoryservice.entity.InventoryBatch;
import com.koerber.inventoryservice.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(
            @PathVariable Long productId) {

        return ResponseEntity.ok(service.getInventory(productId));
    }

    @PostMapping("/reserve")
    public ResponseEntity<InventoryReserveResponse> reserveInventory(
            @RequestBody InventoryRequest request) {

        List<Long> reservedBatchIds =
                service.reserveInventory(request.productId(), request.quantity());

        String productName =
                service.getProductName(request.productId());

        return ResponseEntity.ok(
                new InventoryReserveResponse(
                        request.productId(),
                        productName,
                        reservedBatchIds
                )
        );
    }
}
