package com.github.maximtereshchenko.games.cookies.domain;

import com.github.maximtereshchenko.games.common.event.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public final class BakeryService {

    private final Configuration configuration;
    private final PlayerProgress playerProgress;
    private final EventBus<Event> eventBus;

    public BakeryService(
        Configuration configuration,
        EventBus<Event> eventBus
    ) {
        this.configuration = configuration;
        this.playerProgress = new PlayerProgress();
        this.eventBus = eventBus;
    }

    public void onStart() {
        eventBus.publish(
            new CookieBalanceUpdated(playerProgress.balance())
        );
        eventBus.publish(new BakingRateUpdated(bakingRate()));
        eventBus.publish(new BakingPowerUpdated(bakingPower()));
        for (var building : Building.values()) {
            eventBus.publish(
                new BuildingCountUpdated(
                    building,
                    playerProgress.count(building)
                )
            );
            eventBus.publish(
                new TransactionValueUpdated(
                    building,
                    price(building)
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
        addToBalance(bakingPower());
        unlockBuildings();
        eventBus.publish(new CookiesBaked());
    }

    public void completeTransaction(Building building) {
        addToBalance(price(building).negate());
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
                price(building)
            )
        );
        eventBus.publish(
            new BakingRateUpdated(bakingRate())
        );
        eventBus.publish(new BakingPowerUpdated(bakingPower()));
    }

    public void buyUpgrade(Upgrade upgrade) {
        addToBalance(
            configuration.upgradePrices()
                .get(upgrade)
                .negate()
        );
        playerProgress.activate(upgrade);
        eventBus.publish(
            new BakingRateUpdated(bakingRate())
        );
        eventBus.publish(new BakingPowerUpdated(bakingPower()));
    }

    private BigDecimal bakingPower() {
        return cursorBakingRate(BigDecimal.ONE);
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
                        configuration.upgradePrices()
                            .get(upgrade)
                    )
                );
            }
        }
    }

    private boolean isRequirementSatisfied(Upgrade upgrade) {
        return configuration.upgradeUnlockRequirements()
            .get(upgrade)
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

    private boolean balanceGreaterThanBasePrice(
        Building building
    ) {
        return playerProgress.balance()
                   .compareTo(price(building)) >= 0;
    }

    private BigDecimal bakingRate() {
        var bakingRate = BigDecimal.ZERO;
        for (var building : Building.values()) {
            bakingRate = bakingRate.add(
                bakingRate(building)
                    .multiply(
                        BigDecimal.valueOf(
                            playerProgress.count(building)
                        )
                    )
            );
        }
        return bakingRate;
    }

    private BigDecimal bakingRate(Building building) {
        var baseBakingRate = configuration.buildingBaseBakingRates()
            .get(building);
        return switch (building) {
            case CURSOR -> cursorBakingRate(baseBakingRate);
            case GRANDMA -> grandmaBakingRate(baseBakingRate);
        };
    }

    private BigDecimal grandmaBakingRate(BigDecimal baseBakingRate) {
        var bakingRate = baseBakingRate;
        for (var upgrade : Set.of(Upgrade.GRANDMA_TIER_0, Upgrade.GRANDMA_TIER_1, Upgrade.GRANDMA_TIER_2, Upgrade.GRANDMA_TIER_3, Upgrade.GRANDMA_TIER_4, Upgrade.GRANDMA_TIER_5)) {
            if (playerProgress.isActive(upgrade)) {
                bakingRate = bakingRate.multiply(BigDecimal.TWO);
            }
        }
        return bakingRate;
    }

    private BigDecimal cursorBakingRate(BigDecimal baseBakingRate) {
        var bakingRate = baseBakingRate;
        for (var upgrade : Set.of(Upgrade.CURSOR_TIER_0, Upgrade.CURSOR_TIER_1, Upgrade.CURSOR_TIER_2)) {
            if (playerProgress.isActive(upgrade)) {
                bakingRate = bakingRate.multiply(BigDecimal.TWO);
            }
        }
        return bakingRate.add(nonCursorBuildingBonus());
    }

    private BigDecimal nonCursorBuildingBonus() {
        if (!playerProgress.isActive(Upgrade.CURSOR_TIER_3)) {
            return BigDecimal.ZERO;
        }
        var bonus = BigDecimal.valueOf(0.1)
            .multiply(BigDecimal.valueOf(nonCursorBuildingCount()));
        if (playerProgress.isActive(Upgrade.CURSOR_TIER_4)) {
            bonus = bonus.multiply(BigDecimal.valueOf(5));
        }
        if (playerProgress.isActive(Upgrade.CURSOR_TIER_5)) {
            bonus = bonus.multiply(BigDecimal.valueOf(10));
        }
        return bonus;
    }

    private int nonCursorBuildingCount() {
        var count = 0;
        for (var building : Building.values()) {
            if (building != Building.CURSOR) {
                count += playerProgress.count(building);
            }
        }
        return count;
    }

    private BigDecimal price(Building building) {
        return configuration.buildingBasePrices()
            .get(building)
            .multiply(
                BigDecimal.valueOf(1.15)
                    .pow(playerProgress.count(building))
            )
            .setScale(0, RoundingMode.CEILING);
    }
}
