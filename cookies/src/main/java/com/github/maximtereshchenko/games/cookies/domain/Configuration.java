package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public record Configuration(
    BigDecimal baseBakingPower,
    Map<Building, BuildingConfiguration> buildingConfigurations,
    List<UpgradeTier> upgradeTiers,
    Map<Upgrade, UpgradeConfiguration> upgradeConfigurations,
    Map<Achievement, AchievementUnlockRequirement> achievementUnlockRequirements,
    float milkPercentPerUnlockedAchievement,
    GoldenCookieConfiguration goldenCookieConfiguration
) {

    record BuildingConfiguration(
        BigDecimal basePrice,
        BigDecimal baseBakingRate
    ) {}

    record UpgradeConfiguration(
        UpgradeUnlockRequirement unlockRequirement,
        Price price
    ) {}

    record GoldenCookieConfiguration(
        Duration baseCooldownDuration,
        Duration baseSpawnDuration,
        Duration baseDuration
    ) {}
}
