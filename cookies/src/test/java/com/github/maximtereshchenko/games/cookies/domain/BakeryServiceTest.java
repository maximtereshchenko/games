package com.github.maximtereshchenko.games.cookies.domain;

import com.badlogic.gdx.Preferences;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

final class BakeryServiceTest {

    private final Preferences preferences = mock();
    private final Clock clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);
    private final Random random = mock();
    private final Map<Building, Configuration.BuildingConfiguration> buildingConfigurations =
        new EnumMap<>(Building.class);
    private final Map<Upgrade, Configuration.UpgradeConfiguration> upgradeConfigurations =
        new EnumMap<>(Upgrade.class);
    private final Map<Achievement, AchievementUnlockRequirement>
        achievementUnlockRequirements = new EnumMap<>(Achievement.class);
    private final List<UpgradeTier> upgradeTiers = new ArrayList<>();
    private final Map<Configuration.GoldenCookieConfiguration.EffectType, Float>
        effectChances =
        new EnumMap<>(Configuration.GoldenCookieConfiguration.EffectType.class);
    private final Configuration configuration = configuration();
    private final PlayerProgress playerProgress =
        new PlayerProgress(preferences, clock);
    private final BakeryService bakeryService = new BakeryService(
        configuration,
        playerProgress,
        clock,
        random
    );

    @Test
    void givenElapsedTime_whenCreated_thenOfflineBakingAddedToBalance() {
        when(preferences.contains("last-updated-timestamp")).thenReturn(true);
        when(preferences.getString("last-updated-timestamp"))
            .thenReturn(Instant.EPOCH.toString());
        when(preferences.getInteger("buildings.GRANDMA.count")).thenReturn(1);
        var laterClock = Clock.fixed(
            Instant.EPOCH.plusSeconds(2),
            ZoneOffset.UTC
        );
        var service = new BakeryService(
            configuration,
            new PlayerProgress(preferences, laterClock),
            laterClock,
            random
        );
        assertThat(service.balance()).isEqualByComparingTo("2");
        assertThat(service.cumulativeBaked()).isEqualByComparingTo("2");
    }

    @Test
    void givenBuilding_whenUpdate_thenBalanceIncreasedFromBakingRate() {
        playerProgress.buildingCounts.put(Building.GRANDMA, 1);
        bakeryService.update(2);
        assertThat(bakeryService.balance()).isEqualByComparingTo("2");
        assertThat(bakeryService.goldenCookieInterval())
            .isEqualTo(new Interval(0, 0));
        assertThat(bakeryService.buffInterval(Buff.FRENZY))
            .isEqualTo(new Interval(0, 0));
    }

    @Test
    void givenFractionalCookies_whenBalance_thenFloored() {
        playerProgress.buildingCounts.put(Building.GRANDMA, 1);
        bakeryService.update(0.9f);
        assertThat(bakeryService.balance()).isEqualByComparingTo("0");
        assertThat(bakeryService.cumulativeBaked()).isEqualByComparingTo("0");
    }

    @Test
    void whenBake_thenBakingPowerAddedAndClickCounted() {
        bakeryService.bake();
        assertThat(bakeryService.balance()).isEqualByComparingTo("1");
        assertThat(bakeryService.cumulativeBaked()).isEqualByComparingTo("1");
        assertThat(bakeryService.cumulativeManuallyBaked())
            .isEqualByComparingTo("1");
        assertThat(bakeryService.cumulativeClicks()).isEqualTo(1L);
    }

    @Test
    void givenClickUpgrades_whenBake_thenBakingPowerIncludesBakingRateShare() {
        playerProgress.buildingCounts.put(Building.GRANDMA, 100);
        playerProgress.activeUpgrades.add(Upgrade.CLICK_TIER_0);
        playerProgress.activeUpgrades.add(Upgrade.CLICK_TIER_1);
        bakeryService.bake();
        assertThat(bakeryService.bakingPower()).isEqualByComparingTo("3");
        assertThat(bakeryService.cumulativeManuallyBaked())
            .isEqualByComparingTo("3");
    }

    @Test
    void givenClickFrenzy_whenBakingPower_thenMultiplied() {
        effectChances.clear();
        effectChances.put(
            Configuration.GoldenCookieConfiguration.EffectType.CLICK_FRENZY,
            1f
        );
        bakeryService.goldenCookieEffect();
        assertThat(bakeryService.bakingPower()).isEqualByComparingTo("777");
    }

    @Test
    void givenFrenzyChance_whenGoldenCookieEffect_thenFrenzyBuffReset() {
        assertThat(bakeryService.goldenCookieEffect())
            .isEqualTo(new BuffResetEffect(Buff.FRENZY));
        assertThat(bakeryService.buffEffect(Buff.FRENZY))
            .isEqualTo(new FrenzyEffect(7));
        assertThat(bakeryService.buffInterval(Buff.FRENZY))
            .isEqualTo(new Interval(77, 77));
        assertThat(bakeryService.cumulativeGoldenCookies()).isEqualTo(1);
        assertThat(bakeryService.bakingRate()).isEqualByComparingTo("0");
    }

    @Test
    void givenFrenzyAndBuilding_whenBakingRate_thenMultiplied() {
        playerProgress.buildingCounts.put(Building.GRANDMA, 1);
        bakeryService.goldenCookieEffect();
        assertThat(bakeryService.bakingRate()).isEqualByComparingTo("7");
    }

    @Test
    void givenGoldenCookieDurationUpgrade_whenFrenzy_thenDurationDoubled() {
        playerProgress.activeUpgrades.add(Upgrade.GOLDEN_COOKIE_TIER_2);
        bakeryService.goldenCookieEffect();
        assertThat(bakeryService.buffInterval(Buff.FRENZY))
            .isEqualTo(new Interval(154, 154));
    }

    @Test
    void givenLuckyChance_whenGoldenCookieEffect_thenCookiesAdded() {
        playerProgress.buildingCounts.put(Building.GRANDMA, 1);
        playerProgress.balance = new BigDecimal("1000");
        effectChances.clear();
        effectChances.put(
            Configuration.GoldenCookieConfiguration.EffectType.LUCKY,
            1f
        );
        assertThat(bakeryService.goldenCookieEffect())
            .isInstanceOfSatisfying(
                LuckyGoldenCookieEffect.class,
                effect -> assertThat(effect.amount()).isEqualByComparingTo("163")
            );
        assertThat(bakeryService.balance()).isEqualByComparingTo("1163");
    }

    @Test
    void givenBuildingSpecialChance_whenGoldenCookieEffect_thenBuildingBuffReset() {
        playerProgress.buildingCounts.put(Building.FARM, 3);
        effectChances.clear();
        effectChances.put(
            Configuration.GoldenCookieConfiguration.EffectType.BUILDING_SPECIAL,
            1f
        );
        when(random.nextInt(1)).thenReturn(0);
        assertThat(bakeryService.goldenCookieEffect())
            .isEqualTo(new BuffResetEffect(Buff.BUILDING_SPECIAL));
        assertThat(bakeryService.buffEffect(Buff.BUILDING_SPECIAL))
            .isEqualTo(new BuildingSpecialEffect(Building.FARM, 3, 6));
        assertThat(bakeryService.buffInterval(Buff.BUILDING_SPECIAL))
            .isEqualTo(new Interval(30, 30));
        assertThat(bakeryService.bakingRate())
            .isEqualByComparingTo("18");
    }

    @Test
    void givenNoEligibleBuildings_whenBuildingSpecial_thenFallbackBuff() {
        effectChances.clear();
        effectChances.put(
            Configuration.GoldenCookieConfiguration.EffectType.BUILDING_SPECIAL,
            1f
        );
        assertThat(bakeryService.goldenCookieEffect())
            .isEqualTo(new BuffResetEffect(Buff.FRENZY));
        assertThat(bakeryService.buffEffect(Buff.FRENZY))
            .isEqualTo(new FrenzyEffect(7));
    }

    @Test
    void givenEffectChancesExhausted_whenGoldenCookieEffect_thenIllegalState() {
        effectChances.clear();
        assertThatThrownBy(bakeryService::goldenCookieEffect)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void whenBuyBuilding_thenBalanceDecreasedAndCountIncreased() {
        bakeryService.bake();
        bakeryService.bake();
        bakeryService.completeTransaction(Building.CURSOR, TransactionMode.BUY, 1);
        assertThat(bakeryService.count(Building.CURSOR)).isEqualTo(1);
        assertThat(bakeryService.balance()).isEqualByComparingTo("-8");
        assertThat(bakeryService.totalBuildingCount()).isEqualTo(1);
    }

    @Test
    void whenSellBuilding_thenBalanceIncreasedAndCountDecreased() {
        playerProgress.buildingCounts.put(Building.CURSOR, 2);
        bakeryService.completeTransaction(
            Building.CURSOR,
            TransactionMode.SELL,
            1
        );
        assertThat(bakeryService.count(Building.CURSOR)).isEqualTo(1);
        assertThat(bakeryService.transactionValue(
            Building.CURSOR,
            TransactionMode.SELL,
            1
        )).isEqualByComparingTo("3");
        assertThat(bakeryService.balance()).isEqualByComparingTo("3");
    }

    @Test
    void givenSellExceedsCount_whenCompleteTransaction_thenCountZero() {
        playerProgress.buildingCounts.put(Building.CURSOR, 1);
        bakeryService.completeTransaction(
            Building.CURSOR,
            TransactionMode.SELL,
            5
        );
        assertThat(bakeryService.count(Building.CURSOR)).isZero();
    }

    @Test
    void whenBuyUpgrade_thenBalanceDecreasedAndUpgradeActivated() {
        playerProgress.unlockedUpgrades.add(Upgrade.CLICK_TIER_0);
        bakeryService.bake();
        bakeryService.buyUpgrade(Upgrade.CLICK_TIER_0);
        assertThat(bakeryService.isUnlocked(Upgrade.CLICK_TIER_0)).isFalse();
        assertThat(bakeryService.isActive(Upgrade.CLICK_TIER_0)).isTrue();
        assertThat(bakeryService.balance()).isEqualByComparingTo("0");
    }

    @Test
    void whenPriceExact_thenConfiguredValue() {
        assertThat(bakeryService.price(Upgrade.CLICK_TIER_0))
            .isEqualByComparingTo("1");
    }

    @Test
    void whenPriceTiered_thenBuildingBasePriceTimesTierMultiplier() {
        upgradeConfigurations.put(
            Upgrade.CURSOR_TIER_0,
            new Configuration.UpgradeConfiguration(
                new BuildingCountUnlockRequirement(
                    Map.of(Building.CURSOR, Integer.MAX_VALUE)
                ),
                new TieredPrice(Building.CURSOR, 0)
            )
        );
        assertThat(bakeryService.price(Upgrade.CURSOR_TIER_0))
            .isEqualByComparingTo("50");
    }

    @Test
    void whenTransactionValueBuy_thenSumOfBuildingPrices() {
        assertThat(
            bakeryService.transactionValue(
                Building.CURSOR,
                TransactionMode.BUY,
                2
            )
        ).isEqualByComparingTo("22");
        assertThat(bakeryService.basePrice(Building.CURSOR))
            .isEqualByComparingTo("10");
    }

    @Test
    void givenEnoughBalance_whenCanAffordBuy_thenTrue() {
        playerProgress.balance = new BigDecimal("10");
        assertThat(
            bakeryService.canAfford(Building.CURSOR, TransactionMode.BUY, 1)
        ).isTrue();
        assertThat(bakeryService.canAfford(Upgrade.CLICK_TIER_0)).isTrue();
    }

    @Test
    void givenInsufficientBalance_whenCanAffordBuy_thenFalse() {
        assertThat(
            bakeryService.canAfford(Building.CURSOR, TransactionMode.BUY, 1)
        ).isFalse();
        assertThat(bakeryService.canAfford(Upgrade.CLICK_TIER_0)).isFalse();
    }

    @Test
    void whenCanAffordSell_thenTrue() {
        assertThat(
            bakeryService.canAfford(Building.CURSOR, TransactionMode.SELL, 1)
        ).isTrue();
    }

    @Test
    void whenCreatedTimestamp_thenPlayerProgressTimestamp() {
        assertThat(bakeryService.createdTimestamp()).isEqualTo(Instant.EPOCH);
    }

    @Test
    void givenUnlockedAchievement_whenMilk_thenPercentPerAchievement() {
        playerProgress.unlockedAchievements.add(Achievement.MATHEMATICIAN);
        assertThat(bakeryService.milk()).isEqualTo(0.04f);
        assertThat(bakeryService.isUnlocked(Achievement.MATHEMATICIAN)).isTrue();
    }

    @Test
    void givenKittenUpgrade_whenKittenMultiplier_thenMilkApplied() {
        playerProgress.unlockedAchievements.add(Achievement.MATHEMATICIAN);
        playerProgress.activeUpgrades.add(Upgrade.KITTEN_TIER_0);
        assertThat(bakeryService.kittenMultiplier()).isEqualTo(1.004f);
    }

    @Test
    void whenUpdateVolume_thenVolumeChanged() {
        bakeryService.updateVolume(0.2f);
        assertThat(bakeryService.volume()).isEqualTo(0.2f);
    }

    @Test
    void whenFlush_thenPreferencesFlushed() {
        bakeryService.flush();
        verify(preferences).flush();
    }

    @Test
    void givenBuildingCountRequirement_whenUpdate_thenUpgradeUnlocked() {
        upgradeConfigurations.put(
            Upgrade.CLICK_TIER_0,
            new Configuration.UpgradeConfiguration(
                new BuildingCountUnlockRequirement(Map.of(Building.CURSOR, 1)),
                new ExactPrice(BigDecimal.ONE)
            )
        );
        playerProgress.buildingCounts.put(Building.CURSOR, 1);
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Upgrade.CLICK_TIER_0)).isTrue();
    }

    @Test
    void givenTieredRequirement_whenUpdate_thenUpgradeUnlocked() {
        upgradeConfigurations.put(
            Upgrade.CURSOR_TIER_0,
            new Configuration.UpgradeConfiguration(
                new TieredUnlockRequirement(Building.CURSOR, 0),
                new ExactPrice(BigDecimal.ONE)
            )
        );
        playerProgress.buildingCounts.put(Building.CURSOR, 1);
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Upgrade.CURSOR_TIER_0)).isTrue();
    }

    @Test
    void givenManuallyBakedRequirement_whenUpdate_thenUpgradeUnlocked() {
        upgradeConfigurations.put(
            Upgrade.CLICK_TIER_0,
            new Configuration.UpgradeConfiguration(
                new ManuallyBakedUnlockRequirement(1),
                new ExactPrice(BigDecimal.ONE)
            )
        );
        bakeryService.bake();
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Upgrade.CLICK_TIER_0)).isTrue();
    }

    @Test
    void givenAchievementCountRequirement_whenUpdate_thenUpgradeUnlocked() {
        upgradeConfigurations.put(
            Upgrade.KITTEN_TIER_0,
            new Configuration.UpgradeConfiguration(
                new AchievementCountUnlockRequirement(1),
                new ExactPrice(BigDecimal.ONE)
            )
        );
        playerProgress.unlockedAchievements.add(Achievement.MATHEMATICIAN);
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Upgrade.KITTEN_TIER_0)).isTrue();
    }

    @Test
    void givenGoldenCookieCountRequirement_whenUpdate_thenUpgradeUnlocked() {
        upgradeConfigurations.put(
            Upgrade.GOLDEN_COOKIE_TIER_0,
            new Configuration.UpgradeConfiguration(
                new GoldenCookieCountRequirement(1),
                new ExactPrice(BigDecimal.ONE)
            )
        );
        bakeryService.goldenCookieEffect();
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Upgrade.GOLDEN_COOKIE_TIER_0)).isTrue();
    }

    @Test
    void givenActiveUpgrade_whenUpdate_thenUpgradeNotUnlocked() {
        upgradeConfigurations.put(
            Upgrade.CLICK_TIER_0,
            new Configuration.UpgradeConfiguration(
                new BuildingCountUnlockRequirement(Map.of(Building.CURSOR, 0)),
                new ExactPrice(BigDecimal.ONE)
            )
        );
        playerProgress.activeUpgrades.add(Upgrade.CLICK_TIER_0);
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Upgrade.CLICK_TIER_0)).isFalse();
        assertThat(bakeryService.isActive(Upgrade.CLICK_TIER_0)).isTrue();
    }

    @Test
    void givenCumulativeBakedRequirement_whenUpdate_thenAchievementUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.CUMULATIVE_BAKED_TIER_0,
            new CumulativeBakedUnlockRequirement(BigDecimal.ONE)
        );
        bakeryService.bake();
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.CUMULATIVE_BAKED_TIER_0))
            .isTrue();
    }

    @Test
    void givenBakingRateRequirement_whenUpdate_thenAchievementUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.BAKING_RATE_TIER_0,
            new BakingRateUnlockRequirement(BigDecimal.ONE)
        );
        playerProgress.buildingCounts.put(Building.GRANDMA, 1);
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.BAKING_RATE_TIER_0))
            .isTrue();
    }

    @Test
    void givenManuallyBakedAchievementRequirement_whenUpdate_thenUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.MANUALLY_BAKED_TIER_0,
            new CumulativeManuallyBakedUnlockRequirement(BigDecimal.ONE)
        );
        bakeryService.bake();
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.MANUALLY_BAKED_TIER_0))
            .isTrue();
    }

    @Test
    void givenBuildingCountAchievementRequirement_whenUpdate_thenUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.CURSOR_TIER_0,
            new BuildingCountUnlockRequirement(Map.of(Building.CURSOR, 1))
        );
        playerProgress.buildingCounts.put(Building.CURSOR, 1);
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.CURSOR_TIER_0)).isTrue();
    }

    @Test
    void givenTotalBuildingCountRequirement_whenUpdate_thenAchievementUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.TOTAL_BUILDING_COUNT_TIER_0,
            new TotalBuildingCountUnlockRequirement(2)
        );
        playerProgress.buildingCounts.put(Building.CURSOR, 1);
        playerProgress.buildingCounts.put(Building.GRANDMA, 1);
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.TOTAL_BUILDING_COUNT_TIER_0))
            .isTrue();
    }

    @Test
    void givenTotalUpgradeCountRequirement_whenUpdate_thenAchievementUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.TOTAL_UPGRADE_COUNT_TIER_0,
            new TotalUpgradeCountUnlockRequirement(1)
        );
        playerProgress.activeUpgrades.add(Upgrade.CLICK_TIER_0);
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.TOTAL_UPGRADE_COUNT_TIER_0))
            .isTrue();
    }

    @Test
    void givenEveryBuildingCountRequirement_whenUpdate_thenAchievementUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.EVERY_BUILDING_TIER_0,
            new EveryBuildingCountUnlockRequirement(1)
        );
        for (var building : Building.values()) {
            playerProgress.buildingCounts.put(building, 1);
        }
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.EVERY_BUILDING_TIER_0))
            .isTrue();
    }

    @Test
    void givenMathematicianRequirement_whenUpdate_thenAchievementUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.MATHEMATICIAN,
            new MathematicianUnlockRequirement()
        );
        for (var building : Building.values()) {
            playerProgress.buildingCounts.put(building, 128);
        }
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.MATHEMATICIAN)).isTrue();
    }

    @Test
    void givenBase10Requirement_whenUpdate_thenAchievementUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.BASE_10,
            new Base10UnlockRequirement()
        );
        for (var building : Building.values()) {
            playerProgress.buildingCounts.put(building, 160);
        }
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.BASE_10)).isTrue();
    }

    @Test
    void givenGoldenCookiesAchievementRequirement_whenUpdate_thenUnlocked() {
        achievementUnlockRequirements.put(
            Achievement.GOLDEN_COOKIE_TIER_0,
            new CumulativeGoldenCookiesUnlockRequirement(1)
        );
        bakeryService.goldenCookieEffect();
        bakeryService.update(0);
        assertThat(bakeryService.isUnlocked(Achievement.GOLDEN_COOKIE_TIER_0))
            .isTrue();
    }

    @Test
    void givenGrandmaUpgrade_whenBakingRate_thenDoubled() {
        playerProgress.buildingCounts.put(Building.GRANDMA, 1);
        playerProgress.activeUpgrades.add(Upgrade.GRANDMA_TIER_0);
        assertThat(bakeryService.bakingRate()).isEqualByComparingTo("2");
    }

    @Test
    void givenGrandmaSynergy_whenBakingRate_thenFarmRateIncreased() {
        playerProgress.buildingCounts.put(Building.GRANDMA, 2);
        playerProgress.buildingCounts.put(Building.FARM, 1);
        playerProgress.activeUpgrades.add(Upgrade.GRANDMA_SYNERGY_FARM);
        assertThat(bakeryService.bakingRate()).isEqualByComparingTo("5.02");
    }

    @Test
    void givenCursorNonCursorBonus_whenBakingRate_thenBonusApplied() {
        playerProgress.buildingCounts.put(Building.CURSOR, 1);
        playerProgress.buildingCounts.put(Building.FARM, 1);
        playerProgress.activeUpgrades.add(Upgrade.CURSOR_TIER_3);
        assertThat(bakeryService.bakingRate()).isEqualByComparingTo("2.1");
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    FLAVORED_COOKIE_TIER_0, 1.01
                    FLAVORED_COOKIE_TIER_3, 1.02
                    FLAVORED_COOKIE_TIER_31, 1.03
                    FLAVORED_COOKIE_TIER_37, 1.04
                    FLAVORED_COOKIE_TIER_15, 1.05
                    """
    )
    void givenFlavoredCookie_whenBakingRate_thenMultiplied(
        Upgrade upgrade,
        BigDecimal expected
    ) {
        playerProgress.buildingCounts.put(Building.GRANDMA, 1);
        playerProgress.activeUpgrades.add(upgrade);
        assertThat(bakeryService.bakingRate()).isEqualByComparingTo(expected);
    }

    @ParameterizedTest
    @EnumSource(
        value = Building.class,
        names = "CURSOR",
        mode = EnumSource.Mode.EXCLUDE
    )
    void givenBuilding_whenBakingRate_thenCountTimesBaseRate(Building building) {
        playerProgress.buildingCounts.put(building, 2);
        assertThat(bakeryService.bakingRate()).isEqualByComparingTo("2");
    }

    private Configuration configuration() {
        for (var building : Building.values()) {
            buildingConfigurations.put(
                building,
                new Configuration.BuildingConfiguration(
                    BigDecimal.TEN,
                    BigDecimal.ONE
                )
            );
        }
        var lockedUpgrade = new Configuration.UpgradeConfiguration(
            new BuildingCountUnlockRequirement(
                Map.of(Building.CURSOR, Integer.MAX_VALUE)
            ),
            new ExactPrice(BigDecimal.ONE)
        );
        for (var upgrade : Upgrade.values()) {
            upgradeConfigurations.put(upgrade, lockedUpgrade);
        }
        var lockedAchievement =
            new CumulativeBakedUnlockRequirement(new BigDecimal("1E+100"));
        for (var achievement : Achievement.values()) {
            achievementUnlockRequirements.put(achievement, lockedAchievement);
        }
        upgradeTiers.add(new UpgradeTier(1, new BigDecimal("5")));
        effectChances.put(
            Configuration.GoldenCookieConfiguration.EffectType.FRENZY,
            1f
        );
        return new Configuration(
            BigDecimal.ONE,
            buildingConfigurations,
            upgradeTiers,
            upgradeConfigurations,
            achievementUnlockRequirements,
            0.04f,
            new Configuration.GoldenCookieConfiguration(
                10,
                10,
                13,
                effectChances
            ),
            new Configuration.FrenzyBuffConfiguration(7, 77),
            new Configuration.ClickFrenzyBuffConfiguration(777, 13),
            new Configuration.BuildingSpecialBuffConfiguration(
                1,
                2,
                30,
                Buff.FRENZY
            )
        );
    }
}
