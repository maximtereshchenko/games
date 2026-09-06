package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.util.Map;

public record Configuration(
    BigDecimal baseBakingPower,
    Map<Building, BigDecimal> buildingBasePrices,
    Map<Building, BigDecimal> buildingBaseBakingRates,
    Map<Upgrade, UnlockRequirement> upgradeUnlockRequirements,
    Map<Upgrade, BigDecimal> upgradePrices
) {}
