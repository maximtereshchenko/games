package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record Configuration(
    BigDecimal baseBakingPower,
    Map<Building, BigDecimal> buildingBasePrices,
    Map<Building, BigDecimal> buildingBaseBakingRates,
    List<UpgradeTier> upgradeTiers,
    Map<Upgrade, UnlockRequirement> upgradeUnlockRequirements,
    Map<Upgrade, Price> upgradePrices
) {}
