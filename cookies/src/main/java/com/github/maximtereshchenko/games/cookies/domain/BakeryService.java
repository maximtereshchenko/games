package com.github.maximtereshchenko.games.cookies.domain;

import com.github.maximtereshchenko.games.common.event.EventBus;

import java.math.BigDecimal;

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
        eventBus.publish(new BakingRateUpdated(bakingRate()));
        eventBus.publish(new BakingPowerUpdated(BigDecimal.ONE));
        for (var building : Building.values()) {
            eventBus.publish(
                new TransactionValueUpdated(
                    building,
                    configuration.price(
                        building,
                        playerProgress.count(building)
                    )
                )
            );
        }
    }

    public void update(float deltaTimeSeconds) {
        addToBalance(
            bakingRate()
                .multiply(
                    BigDecimal.valueOf(deltaTimeSeconds)
                )
        );
        unlockBuildings();
    }

    public void bake() {
        addToBalance(BigDecimal.ONE);
        unlockBuildings();
        eventBus.publish(new CookiesBaked());
    }

    public void completeTransaction(Building building) {
        addToBalance(
            configuration.price(
                    building,
                    playerProgress.count(building)
                )
                .negate()
        );
        playerProgress.add(building, 1);
        var count = playerProgress.count(building);
        unlockBuildings();
        unlockUpgrades();
        eventBus.publish(
            new BuildingCountUpdated(
                building,
                count
            )
        );
        eventBus.publish(
            new TransactionValueUpdated(
                building,
                configuration.price(
                    building,
                    count
                )
            )
        );
        eventBus.publish(
            new BakingRateUpdated(bakingRate())
        );
    }

    private void unlockUpgrades() {
        for (var upgrade : Upgrade.values()) {
            if (
                !playerProgress.isUnlocked(upgrade) &&
                isRequirementSatisfied(upgrade)
            ) {
                playerProgress.unlock(upgrade);
                eventBus.publish(
                    new UpgradeUnlocked(upgrade)
                );
                eventBus.publish(
                    new UpgradePriceUpdated(
                        upgrade,
                        configuration.price(upgrade)
                    )
                );
            }
        }
    }

    private boolean isRequirementSatisfied(Upgrade upgrade) {
        return configuration.unlockRequirement(upgrade)
            .isSatisfied(playerProgress);
    }

    private void addToBalance(BigDecimal amount) {
        playerProgress.addToBalance(amount);
        eventBus.publish(
            new CookieBalanceUpdated(playerProgress.balance())
        );
    }

    private void unlockBuildings() {
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
        return playerProgress.balance()
                   .compareTo(
                       configuration.price(
                           building,
                           playerProgress.count(building)
                       )
                   ) >= 0;
    }

    private BigDecimal bakingRate() {
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
