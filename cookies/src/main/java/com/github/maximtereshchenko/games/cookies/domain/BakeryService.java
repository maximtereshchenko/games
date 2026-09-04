package com.github.maximtereshchenko.games.cookies.domain;

import com.github.maximtereshchenko.games.common.event.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BakeryService {

    private final Configuration configuration;
    private final PlayerProgress playerProgress;
    private final EventBus<Event> eventBus;

    public BakeryService(EventBus<Event> eventBus) {
        this.configuration = new Configuration();
        this.playerProgress = new PlayerProgress();
        this.eventBus = eventBus;

    }

    public void onStart() {
        eventBus.publish(
            new CookieBalanceUpdated(playerProgress.balance())
        );
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
        playerProgress.add(building, 1);
        eventBus.publish(
            new BuildingCountUpdated(
                building,
                playerProgress.count(building)
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
        playerProgress.addToBalance(amount);
        eventBus.publish(
            new CookieBalanceUpdated(playerProgress.balance())
        );
        for (var building : Building.values()) {
            if (
                !playerProgress.isUnlocked(building) &&
                balanceGreaterThanBasePrice(building)
            ) {
                playerProgress.unlock(building);
                eventBus.publish(
                    new BuildingUnlocked(building)
                );
            }
        }
    }

    private boolean balanceGreaterThanBasePrice(Building building) {
        return playerProgress.balance().compareTo(configuration.basePrice(building)) >= 0;
    }

    private BigDecimal price(Building building) {
        return configuration.basePrice(building)
            .multiply(
                BigDecimal.valueOf(1.15)
                    .pow(playerProgress.count(building))
            )
            .setScale(0, RoundingMode.CEILING);
    }

    private BigDecimal productionRate() {
        var productionRate = BigDecimal.ZERO;
        for (var building : Building.values()) {
            productionRate = productionRate.add(
                configuration.baseProductionRate(building)
                    .multiply(
                        BigDecimal.valueOf(
                            playerProgress.count(building)
                        )
                    )
            );
        }
        return productionRate;
    }
}
