package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.util.Map;

final class Configuration {

    private final Map<Building, BigDecimal> basePrices = Map.of(
        Building.CURSOR, BigDecimal.valueOf(15)
    );
    private final Map<Building, BigDecimal> baseProductionRates = Map.of(
        Building.CURSOR, BigDecimal.valueOf(0.1)
    );

    BigDecimal basePrice(Building building) {
        return basePrices.get(building);
    }

    BigDecimal baseProductionRate(Building building) {
        return baseProductionRates.get(building);
    }
}
