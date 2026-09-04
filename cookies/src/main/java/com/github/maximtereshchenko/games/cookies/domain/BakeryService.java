package com.github.maximtereshchenko.games.cookies.domain;

import com.github.maximtereshchenko.games.common.event.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public final class BakeryService {

    private final Configuration configuration;
    private final EventBus<Event> eventBus;
    private final Set<Building> unlockedBuildings;
    private final Map<Building, Integer> buildings;
    private BigDecimal balance;

    public BakeryService(EventBus<Event> eventBus) {
        this.configuration = new Configuration();
        this.eventBus = eventBus;
        this.unlockedBuildings = new HashSet<>();
        this.buildings = new EnumMap<>(Building.class);
        this.balance = BigDecimal.ZERO;
        for (var building : Building.values()) {
            buildings.put(building, 0);
        }
    }

    public void onStart() {
        eventBus.publish(new CookieBalanceUpdated(balance));
        eventBus.publish(new BakingRateUpdated(productionRate()));
        eventBus.publish(new BakingPowerUpdated(BigDecimal.ONE));
        for (var building : Building.values()) {
            eventBus.publish(
                new TransactionValueUpdated(
                    building,
                    configuration.basePrice(building)
                )
            );
        }
    }

    public void update(float deltaTimeSeconds) {
        addToBalance(
            productionRate()
                .multiply(
                    BigDecimal.valueOf(deltaTimeSeconds)
                )
        );
    }

    public void bake() {
        addToBalance(BigDecimal.ONE);
        eventBus.publish(new CookiesBaked());
    }

    public void completeTransaction(Building building) {
        addToBalance(price(building).negate());
        eventBus.publish(
            new BuildingCountUpdated(
                building,
                Objects.requireNonNull(
                    buildings.computeIfPresent(
                        building,
                        (_, current) -> current + 1
                    )
                )
            )
        );
        eventBus.publish(
            new TransactionValueUpdated(building, price(building))
        );
        eventBus.publish(
            new BakingRateUpdated(productionRate())
        );
    }

    private void addToBalance(BigDecimal amount) {
        balance = balance.add(amount);
        eventBus.publish(new CookieBalanceUpdated(balance));
        for (var building : Building.values()) {
            if (
                !unlockedBuildings.contains(building) &&
                balance.compareTo(configuration.basePrice(building)) >= 0
            ) {
                unlockedBuildings.add(building);
                eventBus.publish(
                    new BuildingUnlocked(building)
                );
            }
        }
    }

    private BigDecimal price(Building building) {
        return configuration.basePrice(building)
            .multiply(
                BigDecimal.valueOf(1.15)
                    .pow(buildings.get(building))
            )
            .setScale(0, RoundingMode.CEILING);
    }

    private BigDecimal productionRate() {
        var productionRate = BigDecimal.ZERO;
        for (var entry : buildings.entrySet()) {
            productionRate = productionRate.add(
                configuration.baseProductionRate(entry.getKey())
                    .multiply(BigDecimal.valueOf(entry.getValue()))
            );
        }
        return productionRate;
    }
}
