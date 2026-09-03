package com.github.maximtereshchenko.games.cookies.domain;

import com.github.maximtereshchenko.games.common.event.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class BakeryService {

    private static final Map<Building, BigDecimal> BASE_PRICES = Map.of(
        Building.CURSOR, BigDecimal.valueOf(15)
    );
    private static final Map<Building, BigDecimal> BASE_PRODUCTION = Map.of(
        Building.CURSOR, BigDecimal.valueOf(0.1)
    );

    private final EventBus<Event> eventBus;
    private final Map<Building, Integer> buildings;
    private BigDecimal balance;

    public BakeryService(EventBus<Event> eventBus) {
        this.eventBus = eventBus;
        this.buildings = new EnumMap<>(Building.class);
        this.balance = BigDecimal.ZERO;
    }

    public void onStart() {
        eventBus.publish(new CookieBalanceUpdated(balance));
        eventBus.publish(new BakingRateUpdated(productionRate()));
        eventBus.publish(new BakingPowerUpdated(BigDecimal.ONE));
        for (var entry : BASE_PRICES.entrySet()) {
            eventBus.publish(
                new TransactionValueUpdated(
                    entry.getKey(),
                    entry.getValue()
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
        for (var entry : BASE_PRICES.entrySet()) {
            if (
                !buildings.containsKey(entry.getKey()) &&
                balance.compareTo(entry.getValue()) >= 0
            ) {
                buildings.put(entry.getKey(), 0);
                eventBus.publish(
                    new BuildingUnlocked(entry.getKey())
                );
            }
        }
    }

    private BigDecimal price(Building generator) {
        return BASE_PRICES.get(generator)
            .multiply(
                BigDecimal.valueOf(1.15)
                    .pow(buildings.get(generator))
            )
            .setScale(0, RoundingMode.CEILING);
    }

    private BigDecimal productionRate() {
        var productionRate = BigDecimal.ZERO;
        for (var entry : buildings.entrySet()) {
            productionRate = productionRate.add(
                BASE_PRODUCTION.get(entry.getKey())
                    .multiply(BigDecimal.valueOf(entry.getValue()))
            );
        }
        return productionRate;
    }
}
