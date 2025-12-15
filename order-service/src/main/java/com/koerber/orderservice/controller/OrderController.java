package com.koerber.orderservice.controller;

import com.koerber.orderservice.controller.dto.OrderRequest;
import com.koerber.orderservice.controller.dto.OrderResponse;
import com.koerber.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestBody OrderRequest request) {

        OrderResponse response = service.placeOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
