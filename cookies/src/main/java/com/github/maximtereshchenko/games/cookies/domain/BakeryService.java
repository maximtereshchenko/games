package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
        add(
            amount,
            playerProgress::balance,
            playerProgress::setBalance
        );
        add(
            amount,
            playerProgress::cumulativeBaked,
            playerProgress::setCumulativeBaked
        );
        unlockUpgrades();
    }

    public void click() {
        var amount = bakingPower();
        add(
            amount,
            playerProgress::balance,
            playerProgress::setBalance
        );
        add(
            amount,
            playerProgress::cumulativeBaked,
            playerProgress::setCumulativeBaked
        );
        add(
            amount,
            playerProgress::cumulativeManuallyBaked,
            playerProgress::setCumulativeManuallyBaked
        );
    }

    public void completeTransaction(Building building) {
        add(
            transactionValue(building).negate(),
            playerProgress::balance,
            playerProgress::setBalance
        );
        playerProgress.buildings()
            .computeIfPresent(
                building,
                (_, current) -> current + 1
            );
    }

    public void buyUpgrade(Upgrade upgrade) {
        add(
            price(upgrade).negate(),
            playerProgress::balance,
            playerProgress::setBalance
        );
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
        return cursorBakingRate(configuration.baseBakingPower())
            .add(
                calculated(
                    BigDecimal.ZERO,
                    BigDecimal::add,
                    bakingRate()
                        .multiply(BigDecimal.valueOf(0.01)),
                    Upgrade.CLICK_TIER_0,
                    Upgrade.CLICK_TIER_1,
                    Upgrade.CLICK_TIER_2,
                    Upgrade.CLICK_TIER_3,
                    Upgrade.CLICK_TIER_4,
                    Upgrade.CLICK_TIER_5,
                    Upgrade.CLICK_TIER_6,
                    Upgrade.CLICK_TIER_7,
                    Upgrade.CLICK_TIER_8,
                    Upgrade.CLICK_TIER_9,
                    Upgrade.CLICK_TIER_10,
                    Upgrade.CLICK_TIER_11,
                    Upgrade.CLICK_TIER_12,
                    Upgrade.CLICK_TIER_13,
                    Upgrade.CLICK_TIER_14
                )
            );
    }

    public BigDecimal price(Upgrade upgrade) {
        return switch (configuration.upgradePrices().get(upgrade)) {
            case ExactPrice exactPrice -> exactPrice.value();
            case TieredPrice tieredPrice -> configuration.upgradeTiers()
                .get(tieredPrice.tier())
                .basePriceMultiplier()
                .multiply(
                    configuration.buildingBasePrices()
                        .get(tieredPrice.building())
                );
        };
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

    private void add(
        BigDecimal amount,
        Supplier<BigDecimal> getter,
        Consumer<BigDecimal> setter
    ) {
        setter.accept(getter.get().add(amount));
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
        return switch (configuration.upgradeUnlockRequirements().get(upgrade)) {
            case BuildingCountUnlockRequirement requirement -> isRequirementSatisfied(requirement);
            case TieredUnlockRequirement requirement -> isRequirementSatisfied(requirement);
            case ManuallyBakedUnlockRequirement requirement -> isRequirementSatisfied(requirement);
        };
    }

    private boolean isRequirementSatisfied(
        ManuallyBakedUnlockRequirement requirement
    ) {
        return playerProgress.cumulativeManuallyBaked()
                   .compareTo(requirement.count()) >= 0;
    }

    private boolean isRequirementSatisfied(
        TieredUnlockRequirement requirement
    ) {
        return playerProgress.buildings()
                   .get(requirement.building()) >=
               configuration.upgradeTiers()
                   .get(requirement.tier())
                   .buildingCount();
    }

    private boolean isRequirementSatisfied(
        BuildingCountUnlockRequirement requirement
    ) {
        return playerProgress.buildings()
                   .get(requirement.building()) >= requirement.count();
    }

    private BigDecimal bakingRate(Building building) {
        var baseBakingRate = configuration.buildingBaseBakingRates()
            .get(building);
        return switch (building) {
            case CURSOR -> cursorBakingRate(baseBakingRate);
            case GRANDMA -> doubled(
                baseBakingRate,
                Upgrade.GRANDMA_TIER_0,
                Upgrade.GRANDMA_TIER_1,
                Upgrade.GRANDMA_TIER_2,
                Upgrade.GRANDMA_TIER_3,
                Upgrade.GRANDMA_TIER_4,
                Upgrade.GRANDMA_TIER_5,
                Upgrade.GRANDMA_TIER_6,
                Upgrade.GRANDMA_TIER_7,
                Upgrade.GRANDMA_TIER_8,
                Upgrade.GRANDMA_TIER_9,
                Upgrade.GRANDMA_TIER_10,
                Upgrade.GRANDMA_TIER_11,
                Upgrade.GRANDMA_TIER_12,
                Upgrade.GRANDMA_TIER_13,
                Upgrade.GRANDMA_TIER_14
            );
            case FARM -> doubled(
                baseBakingRate,
                Upgrade.FARM_TIER_0,
                Upgrade.FARM_TIER_1,
                Upgrade.FARM_TIER_2,
                Upgrade.FARM_TIER_3,
                Upgrade.FARM_TIER_4,
                Upgrade.FARM_TIER_5,
                Upgrade.FARM_TIER_6,
                Upgrade.FARM_TIER_7,
                Upgrade.FARM_TIER_8,
                Upgrade.FARM_TIER_9,
                Upgrade.FARM_TIER_10,
                Upgrade.FARM_TIER_11,
                Upgrade.FARM_TIER_12,
                Upgrade.FARM_TIER_13,
                Upgrade.FARM_TIER_14
            );
            case MINE -> doubled(
                baseBakingRate,
                Upgrade.MINE_TIER_0,
                Upgrade.MINE_TIER_1,
                Upgrade.MINE_TIER_2,
                Upgrade.MINE_TIER_3,
                Upgrade.MINE_TIER_4,
                Upgrade.MINE_TIER_5,
                Upgrade.MINE_TIER_6,
                Upgrade.MINE_TIER_7,
                Upgrade.MINE_TIER_8,
                Upgrade.MINE_TIER_9,
                Upgrade.MINE_TIER_10,
                Upgrade.MINE_TIER_11,
                Upgrade.MINE_TIER_12,
                Upgrade.MINE_TIER_13,
                Upgrade.MINE_TIER_14
            );
            case FACTORY -> doubled(
                baseBakingRate,
                Upgrade.FACTORY_TIER_0,
                Upgrade.FACTORY_TIER_1,
                Upgrade.FACTORY_TIER_2,
                Upgrade.FACTORY_TIER_3,
                Upgrade.FACTORY_TIER_4,
                Upgrade.FACTORY_TIER_5,
                Upgrade.FACTORY_TIER_6,
                Upgrade.FACTORY_TIER_7,
                Upgrade.FACTORY_TIER_8,
                Upgrade.FACTORY_TIER_9,
                Upgrade.FACTORY_TIER_10,
                Upgrade.FACTORY_TIER_11,
                Upgrade.FACTORY_TIER_12,
                Upgrade.FACTORY_TIER_13,
                Upgrade.FACTORY_TIER_14
            );
            case BANK -> doubled(
                baseBakingRate,
                Upgrade.BANK_TIER_0,
                Upgrade.BANK_TIER_1,
                Upgrade.BANK_TIER_2,
                Upgrade.BANK_TIER_3,
                Upgrade.BANK_TIER_4,
                Upgrade.BANK_TIER_5,
                Upgrade.BANK_TIER_6,
                Upgrade.BANK_TIER_7,
                Upgrade.BANK_TIER_8,
                Upgrade.BANK_TIER_9,
                Upgrade.BANK_TIER_10,
                Upgrade.BANK_TIER_11,
                Upgrade.BANK_TIER_12,
                Upgrade.BANK_TIER_13,
                Upgrade.BANK_TIER_14
            );
            case TEMPLE -> doubled(
                baseBakingRate,
                Upgrade.TEMPLE_TIER_0,
                Upgrade.TEMPLE_TIER_1,
                Upgrade.TEMPLE_TIER_2,
                Upgrade.TEMPLE_TIER_3,
                Upgrade.TEMPLE_TIER_4,
                Upgrade.TEMPLE_TIER_5,
                Upgrade.TEMPLE_TIER_6,
                Upgrade.TEMPLE_TIER_7,
                Upgrade.TEMPLE_TIER_8,
                Upgrade.TEMPLE_TIER_9,
                Upgrade.TEMPLE_TIER_10,
                Upgrade.TEMPLE_TIER_11,
                Upgrade.TEMPLE_TIER_12,
                Upgrade.TEMPLE_TIER_13,
                Upgrade.TEMPLE_TIER_14
            );
        };
    }

    private BigDecimal doubled(
        BigDecimal baseBakingRate,
        Upgrade... upgrades
    ) {
        return multiplied(baseBakingRate, BigDecimal.TWO, upgrades);
    }

    private BigDecimal cursorBakingRate(BigDecimal baseBakingRate) {
        return doubled(
            baseBakingRate,
            Upgrade.CURSOR_TIER_0,
            Upgrade.CURSOR_TIER_1,
            Upgrade.CURSOR_TIER_2
        )
            .add(nonCursorBuildingBonus());
    }

    private BigDecimal nonCursorBuildingBonus() {
        if (!playerProgress.activeUpgrades().contains(Upgrade.CURSOR_TIER_3)) {
            return BigDecimal.ZERO;
        }
        return multiplied(
            multiplied(
                multiplied(
                    BigDecimal.valueOf(0.1)
                        .multiply(BigDecimal.valueOf(nonCursorBuildingCount())),
                    BigDecimal.valueOf(5),
                    Upgrade.CURSOR_TIER_4
                ),
                BigDecimal.valueOf(10),
                Upgrade.CURSOR_TIER_5
            ),
            BigDecimal.valueOf(20),
            Upgrade.CURSOR_TIER_6,
            Upgrade.CURSOR_TIER_7,
            Upgrade.CURSOR_TIER_8,
            Upgrade.CURSOR_TIER_9,
            Upgrade.CURSOR_TIER_10,
            Upgrade.CURSOR_TIER_11,
            Upgrade.CURSOR_TIER_12,
            Upgrade.CURSOR_TIER_13,
            Upgrade.CURSOR_TIER_14
        );
    }

    private BigDecimal multiplied(
        BigDecimal base,
        BigDecimal multiplier,
        Upgrade... upgrades
    ) {
        return calculated(
            base,
            BigDecimal::multiply,
            multiplier,
            upgrades
        );
    }

    private BigDecimal calculated(
        BigDecimal base,
        BinaryOperator<BigDecimal> operator,
        BigDecimal operand,
        Upgrade... upgrades
    ) {
        var calculated = base;
        for (var upgrade : upgrades) {
            if (playerProgress.activeUpgrades().contains(upgrade)) {
                calculated = operator.apply(calculated, operand);
            }
        }
        return calculated;
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
