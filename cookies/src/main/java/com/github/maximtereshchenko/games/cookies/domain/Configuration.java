package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record Configuration(
    BigDecimal baseBakingPower,
    Map<Building, BuildingConfiguration> buildingConfigurations,
    List<UpgradeTier> upgradeTiers,
    Map<Upgrade, UpgradeConfiguration> upgradeConfigurations,
    Map<Achievement, AchievementUnlockRequirement> achievementUnlockRequirements,
    float milkPercentPerUnlockedAchievement,
    GoldenCookieConfiguration goldenCookieConfiguration,
    FrenzyBuffConfiguration frenzyBuffConfiguration
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
        float baseCooldownDurationSeconds,
        float baseSpawnDurationSeconds,
        float baseDurationSeconds,
        Map<EffectType, Float> effectChances
    ) {

        enum EffectType {

            FRENZY,
            LUCKY
        }
    }

    record FrenzyBuffConfiguration(
        float multiplier,
        float baseDurationSeconds
    ) {}
}
