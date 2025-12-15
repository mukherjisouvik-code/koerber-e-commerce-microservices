package com.koerber.orderservice.service;

import com.koerber.orderservice.controller.dto.InventoryRequest;
import com.koerber.orderservice.controller.dto.InventoryReserveResponse;
import com.koerber.orderservice.controller.dto.OrderRequest;
import com.koerber.orderservice.controller.dto.OrderResponse;
import com.koerber.orderservice.entity.Order;
import com.koerber.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    public OrderService(OrderRepository repository,
                        RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public OrderResponse placeOrder(OrderRequest request) {

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setProductId(request.getProductId());
        inventoryRequest.setQuantity(request.getQuantity());

        ResponseEntity<InventoryReserveResponse> responseEntity =
                restTemplate.postForEntity(
                        inventoryServiceUrl + "/inventory/reserve",
                        inventoryRequest,
                        InventoryReserveResponse.class
                );

        InventoryReserveResponse response = responseEntity.getBody();

        if (response == null || response.reservedBatchIds() == null) {
            throw new IllegalStateException("Inventory reservation failed");
        }

        List<Long> reservedBatchIds = response.reservedBatchIds();

        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());
        order.setProductName(response.productName());

        Order savedOrder = repository.save(order);

        return new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getProductId(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                reservedBatchIds
        );
    }
}
