package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public final class BakeryService {

    private final Configuration configuration;
    private final PlayerProgress playerProgress;

    public BakeryService(Configuration configuration) {
        this.configuration = configuration;
        this.playerProgress = new PlayerProgress();
    }

    public void update(float deltaTimeSeconds) {
        var amount = bakingRate()
            .multiply(
                BigDecimal.valueOf(deltaTimeSeconds)
            );
        addToBalance(amount);
        addToCumulativeBaked(amount);
        unlockUpgrades();
    }

    public void bake() {
        var amount = bakingPower();
        addToBalance(amount);
        addToCumulativeBaked(amount);
    }

    public void completeTransaction(Building building) {
        addToBalance(
            transactionValue(building)
                .negate()
        );
        playerProgress.buildings()
            .computeIfPresent(
                building,
                (_, current) -> current + 1
            );
    }

    public void buyUpgrade(Upgrade upgrade) {
        addToBalance(price(upgrade).negate());
        playerProgress.unlockedUpgrades()
            .remove(upgrade);
        playerProgress.activeUpgrades()
            .add(upgrade);
    }

    public int count(Building building) {
        return playerProgress.buildings()
            .get(building);
    }

    public BigDecimal balance() {
        return playerProgress.balance();
    }

    public BigDecimal transactionValue(Building building) {
        return configuration.buildingBasePrices()
            .get(building)
            .multiply(
                BigDecimal.valueOf(1.15)
                    .pow(count(building))
            )
            .setScale(0, RoundingMode.CEILING);
    }

    public boolean isUnlocked(Upgrade upgrade) {
        return playerProgress.unlockedUpgrades()
            .contains(upgrade);
    }

    public BigDecimal bakingRate() {
        var bakingRate = BigDecimal.ZERO;
        for (var building : Building.values()) {
            bakingRate = bakingRate.add(
                bakingRate(building)
                    .multiply(
                        BigDecimal.valueOf(
                            count(building)
                        )
                    )
            );
        }
        return bakingRate;
    }

    public BigDecimal bakingPower() {
        return cursorBakingRate(configuration.baseBakingPower());
    }

    public BigDecimal price(Upgrade upgrade) {
        return configuration.upgradePrices()
            .get(upgrade);
    }

    public boolean canAfford(Building building) {
        return canAfford(transactionValue(building));
    }

    public boolean canAfford(Upgrade upgrade) {
        return canAfford(price(upgrade));
    }

    public BigDecimal cumulativeBaked() {
        return playerProgress.cumulativeBaked();
    }

    private boolean canAfford(BigDecimal value) {
        return balance().compareTo(value) >= 0;
    }

    private void addToBalance(BigDecimal amount) {
        playerProgress.setBalance(
            playerProgress.balance()
                .add(amount)
        );
    }

    private void addToCumulativeBaked(BigDecimal amount) {
        playerProgress.setCumulativeBaked(
            playerProgress.cumulativeBaked()
                .add(amount)
        );
    }

    private void unlockUpgrades() {
        for (var upgrade : Upgrade.values()) {
            if (
                !isUnlocked(upgrade) &&
                !playerProgress.activeUpgrades().contains(upgrade) &&
                isRequirementSatisfied(upgrade)
            ) {
                playerProgress.unlockedUpgrades()
                    .add(upgrade);
            }
        }
    }

    private boolean isRequirementSatisfied(Upgrade upgrade) {
        return configuration.upgradeUnlockRequirements()
            .get(upgrade)
            .isSatisfied(playerProgress);
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
            if (playerProgress.activeUpgrades().contains(upgrade)) {
                bakingRate = bakingRate.multiply(BigDecimal.TWO);
            }
        }
        return bakingRate;
    }

    private BigDecimal cursorBakingRate(BigDecimal baseBakingRate) {
        var bakingRate = baseBakingRate;
        for (var upgrade : Set.of(Upgrade.CURSOR_TIER_0, Upgrade.CURSOR_TIER_1, Upgrade.CURSOR_TIER_2)) {
            if (playerProgress.activeUpgrades().contains(upgrade)) {
                bakingRate = bakingRate.multiply(BigDecimal.TWO);
            }
        }
        return bakingRate.add(nonCursorBuildingBonus());
    }

    private BigDecimal nonCursorBuildingBonus() {
        var activeUpgrades = playerProgress.activeUpgrades();
        if (!activeUpgrades.contains(Upgrade.CURSOR_TIER_3)) {
            return BigDecimal.ZERO;
        }
        var bonus = BigDecimal.valueOf(0.1)
            .multiply(BigDecimal.valueOf(nonCursorBuildingCount()));
        if (activeUpgrades.contains(Upgrade.CURSOR_TIER_4)) {
            bonus = bonus.multiply(BigDecimal.valueOf(5));
        }
        if (activeUpgrades.contains(Upgrade.CURSOR_TIER_5)) {
            bonus = bonus.multiply(BigDecimal.valueOf(10));
        }
        return bonus;
    }

    private int nonCursorBuildingCount() {
        var count = 0;
        for (var building : Building.values()) {
            if (building != Building.CURSOR) {
                count += count(building);
            }
        }
        return count;
    }
}
