package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

final class Configuration {

    private final Map<Building, BigDecimal> buildingBasePrices =
        Map.of(
            Building.CURSOR, BigDecimal.valueOf(15)
        );
    private final Map<Building, BigDecimal> buildingBaseProductionRates =
        Map.of(
            Building.CURSOR, BigDecimal.valueOf(0.1)
        );
    private final Map<Upgrade, UnlockRequirement> upgradeUnlockRequirements =
        Map.of(
            Upgrade.CURSOR_TIER_0,
            new BuildingCountRequirement(Building.CURSOR, 1),
            Upgrade.CURSOR_TIER_1,
            new BuildingCountRequirement(Building.CURSOR, 1),
            Upgrade.CURSOR_TIER_2,
            new BuildingCountRequirement(Building.CURSOR, 10),
            Upgrade.CURSOR_TIER_3,
            new BuildingCountRequirement(Building.CURSOR, 25),
            Upgrade.CURSOR_TIER_4,
            new BuildingCountRequirement(Building.CURSOR, 50),
            Upgrade.CURSOR_TIER_5,
            new BuildingCountRequirement(Building.CURSOR, 100)
        );
    private final Map<Upgrade, BigDecimal> upgradePrices =
        Map.of(
            Upgrade.CURSOR_TIER_0, BigDecimal.valueOf(100),
            Upgrade.CURSOR_TIER_1, BigDecimal.valueOf(500),
            Upgrade.CURSOR_TIER_2, BigDecimal.valueOf(10000),
            Upgrade.CURSOR_TIER_3, BigDecimal.valueOf(100000),
            Upgrade.CURSOR_TIER_4, BigDecimal.valueOf(10000000),
            Upgrade.CURSOR_TIER_5, BigDecimal.valueOf(100000000)
        );

    BigDecimal price(Building building, int count) {
        return buildingBasePrices.get(building)
            .multiply(
                BigDecimal.valueOf(1.15)
                    .pow(count)
            )
            .setScale(0, RoundingMode.CEILING);
    }

    BigDecimal baseProductionRate(Building building) {
        return buildingBaseProductionRates.get(building);
    }

    UnlockRequirement unlockRequirement(Upgrade upgrade) {
        return upgradeUnlockRequirements.get(upgrade);
    }

    BigDecimal price(Upgrade upgrade) {
        return upgradePrices.get(upgrade);
    }
}
