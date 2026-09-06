package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

final class Configuration {

    private final Map<Building, BigDecimal> buildingBasePrices =
        Map.of(
            Building.CURSOR, BigDecimal.valueOf(15),
            Building.GRANDMA, BigDecimal.valueOf(100)
        );
    private final Map<Building, BigDecimal> buildingBaseBakingRates =
        Map.of(
            Building.CURSOR, BigDecimal.valueOf(0.1),
            Building.GRANDMA, BigDecimal.valueOf(1)
        );
    private final Map<Upgrade, UnlockRequirement> upgradeUnlockRequirements =
        upgradeUnlockRequirements();
    private final Map<Upgrade, BigDecimal> upgradePrices =
        upgradePrices();

    BigDecimal basePrice(Building building) {
        return buildingBasePrices.get(building);
    }

    BigDecimal baseBakingRate(Building building) {
        return buildingBaseBakingRates.get(building);
    }

    UnlockRequirement unlockRequirement(Upgrade upgrade) {
        return upgradeUnlockRequirements.get(upgrade);
    }

    BigDecimal price(Upgrade upgrade) {
        return upgradePrices.get(upgrade);
    }

    private Map<Upgrade, UnlockRequirement> upgradeUnlockRequirements() {
        var map = new EnumMap<Upgrade, UnlockRequirement>(Upgrade.class);
        map.put(
            Upgrade.CURSOR_TIER_0,
            new BuildingCountRequirement(Building.CURSOR, 1)
        );
        map.put(
            Upgrade.CURSOR_TIER_1,
            new BuildingCountRequirement(Building.CURSOR, 1)
        );
        map.put(
            Upgrade.CURSOR_TIER_2,
            new BuildingCountRequirement(Building.CURSOR, 10)
        );
        map.put(
            Upgrade.CURSOR_TIER_3,
            new BuildingCountRequirement(Building.CURSOR, 25)
        );
        map.put(
            Upgrade.CURSOR_TIER_4,
            new BuildingCountRequirement(Building.CURSOR, 50)
        );
        map.put(
            Upgrade.CURSOR_TIER_5,
            new BuildingCountRequirement(Building.CURSOR, 100)
        );
        map.put(
            Upgrade.GRANDMA_TIER_0,
            new BuildingCountRequirement(Building.GRANDMA, 1)
        );
        map.put(
            Upgrade.GRANDMA_TIER_1,
            new BuildingCountRequirement(Building.GRANDMA, 5)
        );
        map.put(
            Upgrade.GRANDMA_TIER_2,
            new BuildingCountRequirement(Building.GRANDMA, 25)
        );
        map.put(
            Upgrade.GRANDMA_TIER_3,
            new BuildingCountRequirement(Building.GRANDMA, 50)
        );
        map.put(
            Upgrade.GRANDMA_TIER_4,
            new BuildingCountRequirement(Building.GRANDMA, 100)
        );
        map.put(
            Upgrade.GRANDMA_TIER_5,
            new BuildingCountRequirement(Building.GRANDMA, 150)
        );
        return map;
    }

    private Map<Upgrade, BigDecimal> upgradePrices() {
        var map = new EnumMap<Upgrade, BigDecimal>(Upgrade.class);
        map.put(
            Upgrade.CURSOR_TIER_0,
            BigDecimal.valueOf(100)
        );
        map.put(
            Upgrade.CURSOR_TIER_1,
            BigDecimal.valueOf(500)
        );
        map.put(
            Upgrade.CURSOR_TIER_2,
            BigDecimal.valueOf(10000)
        );
        map.put(
            Upgrade.CURSOR_TIER_3,
            BigDecimal.valueOf(100000)
        );
        map.put(
            Upgrade.CURSOR_TIER_4,
            BigDecimal.valueOf(10000000)
        );
        map.put(
            Upgrade.CURSOR_TIER_5,
            BigDecimal.valueOf(100000000)
        );
        map.put(
            Upgrade.GRANDMA_TIER_0,
            BigDecimal.valueOf(1000)
        );
        map.put(
            Upgrade.GRANDMA_TIER_1,
            BigDecimal.valueOf(5000)
        );
        map.put(
            Upgrade.GRANDMA_TIER_2,
            BigDecimal.valueOf(50000)
        );
        map.put(
            Upgrade.GRANDMA_TIER_3,
            BigDecimal.valueOf(5000000)
        );
        map.put(
            Upgrade.GRANDMA_TIER_4,
            BigDecimal.valueOf(500000000)
        );
        map.put(
            Upgrade.GRANDMA_TIER_5,
            new BigDecimal("50000000000")
        );
        return map;
    }
}
