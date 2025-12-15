package com.koerber.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koerber.inventoryservice.controller.dto.InventoryRequest;
import com.koerber.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getInventory_shouldReturnInventory() throws Exception {

        mockMvc.perform(get("/inventory/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1001))
                .andExpect(jsonPath("$.productName").exists())
                .andExpect(jsonPath("$.batches").isArray());
    }

    @Test
    void reserveInventory_shouldReserveStock() throws Exception {

        InventoryRequest request =
                new InventoryRequest(1001L, 2);

        mockMvc.perform(post("/inventory/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1001))
                .andExpect(jsonPath("$.reservedBatchIds").isArray());
    }
}
