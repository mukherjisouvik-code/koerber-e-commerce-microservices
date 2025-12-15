package com.koerber.inventoryservice.factory;

import com.koerber.inventoryservice.enums.AllocationStrategyType;
import com.koerber.inventoryservice.strategy.InventoryAllocationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class InventoryStrategyFactory {

    private final Map<AllocationStrategyType, InventoryAllocationStrategy> strategies;

    public InventoryStrategyFactory(
            List<InventoryAllocationStrategy> strategyList) {

        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        InventoryAllocationStrategy::getType,
                        Function.identity()
                ));
    }

    public InventoryAllocationStrategy getStrategy(AllocationStrategyType type) {
        return strategies.get(type);
    }
}
