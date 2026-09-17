package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.stream.Stream;

public final class BakeryService {

    private final Configuration configuration;
    private final PlayerProgress playerProgress;
    private final Random random;
    private final GoldenCookie goldenCookie;
    private final Map<Buff, ActiveBuff> buffs;

    public BakeryService(
        Configuration configuration,
        PlayerProgress playerProgress,
        Clock clock,
        Random random
    ) {
        this.configuration = configuration;
        this.playerProgress = playerProgress;
        this.random = random;
        this.goldenCookie = new GoldenCookie(
            configuration.goldenCookieConfiguration(),
            playerProgress,
            random
        );
        this.buffs = new EnumMap<>(Buff.class);
        for (var buff : Buff.values()) {
            buffs.put(buff, new ActiveBuff());
        }
        updatePlayerProgress(
            (double) Duration.between(
                    playerProgress.lastFlushTimestamp,
                    Instant.now(clock)
                )
                .toMillis() / TimeUnit.SECONDS.toMillis(1)
        );
    }

    public void update(float deltaTimeSeconds) {
        updatePlayerProgress(deltaTimeSeconds);
        goldenCookie.update(deltaTimeSeconds);
        for (var buff : buffs.values()) {
            buff.update(deltaTimeSeconds);
        }
    }

    public void bake() {
        var amount = bakingPower();
        addToBalance(amount);
        playerProgress.cumulativeManuallyBaked =
            playerProgress.cumulativeManuallyBaked.add(
                amount
            );
        playerProgress.cumulativeClicks++;
    }

    public GoldenCookieEffect goldenCookieEffect() {
        goldenCookie.reset();
        playerProgress.cumulativeGoldenCookies++;
        return goldenCookieEffect(effectType());
    }

    public BuffEffect buffEffect(Buff buff) {
        return buffs.get(buff).buffEffect();
    }

    public Interval buffInterval(Buff buff) {
        return buffs.get(buff).interval();
    }

    public void completeTransaction(
        Building building,
        TransactionMode transactionMode,
        int amount
    ) {
        var transactionValue = transactionValue(
            building,
            transactionMode,
            amount
        );
        playerProgress.balance =
            playerProgress.balance.add(
                switch (transactionMode) {
                    case BUY -> transactionValue.negate();
                    case SELL -> transactionValue;
                }

            );
        playerProgress.buildingCounts
            .computeIfPresent(
                building,
                (_, current) -> switch (transactionMode) {
                    case BUY -> current + amount;
                    case SELL -> Math.max(0, current - amount);
                }
            );
    }

    public void buyUpgrade(Upgrade upgrade) {
        playerProgress.balance =
            playerProgress.balance.subtract(
                price(upgrade)
            );
        playerProgress.unlockedUpgrades.remove(upgrade);
        playerProgress.activeUpgrades.add(upgrade);
    }

    public int count(Building building) {
        return playerProgress.buildingCounts.get(building);
    }

    public int totalBuildingCount() {
        return Stream.of(Building.values())
            .mapToInt(this::count)
            .sum();
    }

    public BigDecimal balance() {
        return rounded(
            playerProgress.balance,
            RoundingMode.FLOOR
        );
    }

    public BigDecimal transactionValue(
        Building building,
        TransactionMode transactionMode,
        int amount
    ) {
        var count = count(building);
        return switch (transactionMode) {
            case BUY -> price(
                building,
                count,
                count + amount
            );
            case SELL -> rounded(
                price(
                    building,
                    Math.max(0, count - amount),
                    count
                )
                    .multiply(BigDecimal.valueOf(0.25)),
                RoundingMode.CEILING
            );
        };
    }

    public boolean isUnlocked(Upgrade upgrade) {
        return playerProgress.unlockedUpgrades
            .contains(upgrade);
    }

    public BigDecimal bakingRate() {
        return buffedBakingRate(
            withMilk(
                multiplied(
                    multiplied(
                        multiplied(
                            multiplied(
                                multiplied(
                                    buildingsBakingRate(),
                                    BigDecimal.valueOf(1.01),
                                    Upgrade.FLAVORED_COOKIE_TIER_0,
                                    Upgrade.FLAVORED_COOKIE_TIER_1,
                                    Upgrade.FLAVORED_COOKIE_TIER_2
                                ),
                                BigDecimal.valueOf(1.02),
                                Upgrade.FLAVORED_COOKIE_TIER_3,
                                Upgrade.FLAVORED_COOKIE_TIER_4,
                                Upgrade.FLAVORED_COOKIE_TIER_5,
                                Upgrade.FLAVORED_COOKIE_TIER_6,
                                Upgrade.FLAVORED_COOKIE_TIER_7,
                                Upgrade.FLAVORED_COOKIE_TIER_8,
                                Upgrade.FLAVORED_COOKIE_TIER_9,
                                Upgrade.FLAVORED_COOKIE_TIER_10,
                                Upgrade.FLAVORED_COOKIE_TIER_11,
                                Upgrade.FLAVORED_COOKIE_TIER_12,
                                Upgrade.FLAVORED_COOKIE_TIER_13,
                                Upgrade.FLAVORED_COOKIE_TIER_14,
                                Upgrade.FLAVORED_COOKIE_TIER_17,
                                Upgrade.FLAVORED_COOKIE_TIER_18,
                                Upgrade.FLAVORED_COOKIE_TIER_19,
                                Upgrade.FLAVORED_COOKIE_TIER_20,
                                Upgrade.FLAVORED_COOKIE_TIER_21,
                                Upgrade.FLAVORED_COOKIE_TIER_22,
                                Upgrade.FLAVORED_COOKIE_TIER_23,
                                Upgrade.FLAVORED_COOKIE_TIER_24,
                                Upgrade.FLAVORED_COOKIE_TIER_25,
                                Upgrade.FLAVORED_COOKIE_TIER_26,
                                Upgrade.FLAVORED_COOKIE_TIER_27,
                                Upgrade.FLAVORED_COOKIE_TIER_28
                            ),
                            BigDecimal.valueOf(1.03),
                            Upgrade.FLAVORED_COOKIE_TIER_31,
                            Upgrade.FLAVORED_COOKIE_TIER_32,
                            Upgrade.FLAVORED_COOKIE_TIER_33,
                            Upgrade.FLAVORED_COOKIE_TIER_34,
                            Upgrade.FLAVORED_COOKIE_TIER_35,
                            Upgrade.FLAVORED_COOKIE_TIER_36
                        ),
                        BigDecimal.valueOf(1.04),
                        Upgrade.FLAVORED_COOKIE_TIER_37,
                        Upgrade.FLAVORED_COOKIE_TIER_38
                    ),
                    BigDecimal.valueOf(1.05),
                    Upgrade.FLAVORED_COOKIE_TIER_15,
                    Upgrade.FLAVORED_COOKIE_TIER_16,
                    Upgrade.FLAVORED_COOKIE_TIER_29,
                    Upgrade.FLAVORED_COOKIE_TIER_30
                )
            )
        );
    }

    public BigDecimal bakingPower() {
        var bakingPower = cursorBakingRate(configuration.baseBakingPower())
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
                    Upgrade.CLICK_TIER_8
                )
            );
        if (activeBuffEffect(Buff.CLICK_FRENZY).orElse(null) instanceof ClickFrenzyEffect clickFrenzyEffect) {
            return bakingPower.multiply(
                BigDecimal.valueOf(clickFrenzyEffect.multiplier())
            );
        }
        return bakingPower;
    }

    public BigDecimal price(Upgrade upgrade) {
        return switch (configuration.upgradeConfigurations().get(upgrade).price()) {
            case ExactPrice exactPrice -> exactPrice.value();
            case TieredPrice tieredPrice -> configuration.upgradeTiers()
                .get(tieredPrice.tier())
                .basePriceMultiplier()
                .multiply(
                    configuration.buildingConfigurations()
                        .get(tieredPrice.building())
                        .basePrice()
                );
        };
    }

    public boolean canAfford(
        Building building,
        TransactionMode transactionMode,
        int amount
    ) {
        return switch (transactionMode) {
            case BUY -> canAfford(
                transactionValue(
                    building,
                    transactionMode,
                    amount
                )
            );
            case SELL -> true;
        };
    }

    public boolean canAfford(Upgrade upgrade) {
        return canAfford(price(upgrade));
    }

    public BigDecimal cumulativeBaked() {
        return rounded(
            playerProgress.cumulativeBaked,
            RoundingMode.FLOOR
        );
    }

    public Instant createdTimestamp() {
        return playerProgress.createdTimestamp;
    }

    public long cumulativeClicks() {
        return playerProgress.cumulativeClicks;
    }

    public BigDecimal cumulativeManuallyBaked() {
        return playerProgress.cumulativeManuallyBaked;
    }

    public boolean isActive(Upgrade upgrade) {
        return playerProgress.activeUpgrades
            .contains(upgrade);
    }

    public boolean isUnlocked(Achievement achievement) {
        return playerProgress.unlockedAchievements
            .contains(achievement);
    }

    public BigDecimal basePrice(Building building) {
        return price(building, 0, 1);
    }

    public float milk() {
        return configuration.milkPercentPerUnlockedAchievement() *
               playerProgress.unlockedAchievements.size();
    }

    public Interval goldenCookieInterval() {
        return goldenCookie.interval();
    }

    private BigDecimal buffedBakingRate(BigDecimal base) {
        var bakingRate = base;
        for (var buff : Buff.values()) {
            bakingRate = bakingRate.multiply(
                BigDecimal.valueOf(
                    switch (activeBuffEffect(buff).orElse(null)) {
                        case BuildingSpecialEffect buildingSpecialEffect -> buildingSpecialEffect.multiplier();
                        case FrenzyEffect frenzyEffect -> frenzyEffect.multiplier();
                        case null, default -> 1;
                    }
                )
            );
        }
        return bakingRate;
    }

    private GoldenCookieEffect goldenCookieEffect(
        Configuration.GoldenCookieConfiguration.EffectType effectType
    ) {
        return switch (effectType) {
            case FRENZY -> buffResetEffect(Buff.FRENZY);
            case CLICK_FRENZY -> buffResetEffect(Buff.CLICK_FRENZY);
            case LUCKY -> {
                var amount = BinaryOperator.<BigDecimal>minBy(
                        Comparator.naturalOrder()
                    )
                    .apply(
                        playerProgress.balance.multiply(
                            BigDecimal.valueOf(0.15)
                        ),
                        bakingRate()
                            .multiply(
                                new BigDecimal(
                                    TimeUnit.MINUTES.toSeconds(15)
                                )
                            )
                    )
                    .add(new BigDecimal(13));
                addToBalance(amount);
                yield new LuckyGoldenCookieEffect(amount);
            }
            case BUILDING_SPECIAL -> buffResetEffect(Buff.BUILDING_SPECIAL);
        };
    }

    private List<Building> buildingSpecialBuffEligibleBuildings(int minBuildingCount) {
        return Stream.of(Building.values())
            .filter(building -> count(building) >= minBuildingCount)
            .toList();
    }

    private Optional<BuffEffect> activeBuffEffect(Buff buff) {
        var activeBuff = buffs.get(buff);
        if (activeBuff.interval().progress() < 1) {
            return Optional.of(activeBuff.buffEffect());
        }
        return Optional.empty();
    }

    private BuffResetEffect buffResetEffect(Buff buff) {
        switch (buff) {
            case FRENZY -> {
                var frenzyBuffConfiguration = configuration.frenzyBuffConfiguration();
                buffs.get(buff)
                    .reset(
                        new FrenzyEffect(
                            frenzyBuffConfiguration.multiplier()
                        ),
                        increasedDuration(
                            frenzyBuffConfiguration.baseDurationSeconds()
                        )
                    );
            }
            case CLICK_FRENZY -> {
                var clickFrenzyBuffConfiguration = configuration.clickFrenzyBuffConfiguration();
                buffs.get(buff)
                    .reset(
                        new ClickFrenzyEffect(
                            clickFrenzyBuffConfiguration.multiplier()
                        ),
                        increasedDuration(
                            clickFrenzyBuffConfiguration.baseDurationSeconds()
                        )
                    );
            }
            case BUILDING_SPECIAL -> {
                var buildingSpecialBuffConfiguration =
                    configuration.buildingSpecialBuffConfiguration();
                var buildings = buildingSpecialBuffEligibleBuildings(
                    buildingSpecialBuffConfiguration.minBuildingCount()
                );
                if (buildings.isEmpty()) {
                    return buffResetEffect(
                        buildingSpecialBuffConfiguration.fallback()
                    );
                }
                var building = buildings.get(random.nextInt(buildings.size()));
                var count = count(building);
                buffs.get(buff)
                    .reset(
                        new BuildingSpecialEffect(
                            building,
                            count,
                            buildingSpecialBuffConfiguration.multiplierPerBuilding() * count
                        ),
                        increasedDuration(
                            buildingSpecialBuffConfiguration.baseDurationSeconds()
                        )
                    );
            }
        }
        return new BuffResetEffect(buff);
    }

    private float increasedDuration(float base) {
        if (isActive(Upgrade.GOLDEN_COOKIE_TIER_2)) {
            return base * 2;
        }
        return base;
    }

    private void addToBalance(BigDecimal amount) {
        playerProgress.balance =
            playerProgress.balance.add(
                amount
            );
        playerProgress.cumulativeBaked =
            playerProgress.cumulativeBaked.add(
                amount
            );
    }

    private Configuration.GoldenCookieConfiguration.EffectType effectType() {
        var chance = random.nextFloat();
        var chances = configuration.goldenCookieConfiguration()
            .effectChances()
            .entrySet();
        for (var entry : chances) {
            chance -= entry.getValue();
            if (chance < 0) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException();
    }

    private BigDecimal buildingsBakingRate() {
        var bakingRate = BigDecimal.ZERO;
        for (var building : Building.values()) {
            bakingRate = bakingRate.add(
                bakingRate(building)
                    .multiply(
                        new BigDecimal(count(building))
                    )
            );
        }
        return bakingRate;
    }

    private void updatePlayerProgress(double deltaTimeSeconds) {
        var amount = bakingRate()
            .multiply(BigDecimal.valueOf(deltaTimeSeconds));
        addToBalance(amount);
        unlockUpgrades();
        unlockAchievements();
    }

    private BigDecimal price(Building building, int from, int to) {
        var price = BigDecimal.ZERO;
        for (var i = from; i < to; i++) {
            price = price.add(
                rounded(
                    configuration.buildingConfigurations()
                        .get(building)
                        .basePrice()
                        .multiply(
                            BigDecimal.valueOf(1.15)
                                .pow(i)
                        ),
                    RoundingMode.CEILING
                )
            );
        }
        return price;
    }

    private void unlockAchievements() {
        for (var achievement : Achievement.values()) {
            if (isRequirementSatisfied(achievement)) {
                playerProgress.unlockedAchievements
                    .add(achievement);
            }
        }
    }

    private boolean isRequirementSatisfied(Achievement achievement) {
        return switch (configuration.achievementUnlockRequirements().get(achievement)) {
            case CumulativeBakedUnlockRequirement requirement -> isRequirementSatisfied(
                requirement
            );
            case BakingRateUnlockRequirement requirement -> isRequirementSatisfied(requirement);
            case CumulativeManuallyBakedUnlockRequirement requirement -> isRequirementSatisfied(
                requirement
            );
            case BuildingCountUnlockRequirement requirement -> isRequirementSatisfied(
                requirement
            );
            case TotalBuildingCountUnlockRequirement requirement -> isRequirementSatisfied(
                requirement
            );
            case TotalUpgradeCountUnlockRequirement requirement -> isRequirementSatisfied(
                requirement
            );
            case EveryBuildingCountUnlockRequirement requirement -> isRequirementSatisfied(
                requirement
            );
            case MathematicianUnlockRequirement _ -> isUnlockRequirementSatisfied(
                1,
                count -> Math.min(128, count * 2)
            );
            case Base10UnlockRequirement _ -> isUnlockRequirementSatisfied(
                10,
                count -> count + 10
            );
            case CumulativeGoldenCookiesUnlockRequirement requirement -> isRequirementSatisfied(
                requirement
            );
        };
    }

    private boolean isRequirementSatisfied(
        CumulativeGoldenCookiesUnlockRequirement requirement
    ) {
        return playerProgress.cumulativeGoldenCookies >= requirement.count();
    }

    private boolean isUnlockRequirementSatisfied(int start, IntUnaryOperator operator) {
        var count = start;
        var buildings = Building.values();
        for (var i = buildings.length - 1; i >= 0; i--) {
            if (count(buildings[i]) < count) {
                return false;
            }
            count = operator.applyAsInt(count);
        }
        return true;
    }

    private boolean isRequirementSatisfied(
        EveryBuildingCountUnlockRequirement requirement
    ) {
        return Stream.of(Building.values())
            .map(this::count)
            .allMatch(count -> count >= requirement.count());
    }

    private boolean isRequirementSatisfied(
        TotalUpgradeCountUnlockRequirement requirement
    ) {
        return Stream.of(Upgrade.values())
                   .filter(this::isActive)
                   .count() >= requirement.count();
    }

    private boolean isRequirementSatisfied(
        TotalBuildingCountUnlockRequirement requirement
    ) {
        return totalBuildingCount() >= requirement.count();
    }

    private boolean isRequirementSatisfied(BakingRateUnlockRequirement requirement) {
        return bakingRate().compareTo(requirement.value()) >= 0;
    }

    private boolean isRequirementSatisfied(
        CumulativeBakedUnlockRequirement requirement
    ) {
        return playerProgress.cumulativeBaked
                   .compareTo(requirement.value()) >= 0;
    }

    private boolean isRequirementSatisfied(
        CumulativeManuallyBakedUnlockRequirement requirement
    ) {
        return playerProgress.cumulativeManuallyBaked
                   .compareTo(requirement.value()) >= 0;
    }

    private BigDecimal rounded(
        BigDecimal value,
        RoundingMode roundingMode
    ) {
        return value.setScale(0, roundingMode);
    }

    private boolean canAfford(BigDecimal value) {
        return playerProgress.balance.compareTo(value) >= 0;
    }

    private void unlockUpgrades() {
        for (var upgrade : Upgrade.values()) {
            if (
                !isUnlocked(upgrade) &&
                !isActive(upgrade) &&
                isRequirementSatisfied(upgrade)
            ) {
                playerProgress.unlockedUpgrades
                    .add(upgrade);
            }
        }
    }

    private boolean isRequirementSatisfied(Upgrade upgrade) {
        return switch (configuration.upgradeConfigurations().get(upgrade).unlockRequirement()) {
            case BuildingCountUnlockRequirement requirement -> isRequirementSatisfied(requirement);
            case TieredUnlockRequirement requirement -> isRequirementSatisfied(requirement);
            case ManuallyBakedUnlockRequirement manuallyBakedUnlockRequirement -> isRequirementSatisfied(
                manuallyBakedUnlockRequirement,
                upgrade
            );
            case AchievementCountUnlockRequirement requirement -> isRequirementSatisfied(requirement);
            case GoldenCookieCountRequirement requirement -> isRequirementSatisfied(requirement);
        };
    }

    private boolean isRequirementSatisfied(
        GoldenCookieCountRequirement requirement
    ) {
        return playerProgress.cumulativeGoldenCookies >= requirement.count();
    }

    private boolean isRequirementSatisfied(
        AchievementCountUnlockRequirement requirement
    ) {
        return Stream.of(Achievement.values())
                   .filter(this::isUnlocked)
                   .count() >= requirement.count();
    }

    private boolean isRequirementSatisfied(
        ManuallyBakedUnlockRequirement requirement,
        Upgrade upgrade
    ) {
        return playerProgress.cumulativeManuallyBaked
                   .compareTo(
                       price(upgrade)
                           .multiply(
                               BigDecimal.valueOf(
                                   requirement.percent()
                               )
                           )
                   ) >= 0;
    }

    private boolean isRequirementSatisfied(
        TieredUnlockRequirement requirement
    ) {
        return playerProgress.buildingCounts
                   .get(requirement.building()) >=
               configuration.upgradeTiers()
                   .get(requirement.tier())
                   .buildingCount();
    }

    private boolean isRequirementSatisfied(
        BuildingCountUnlockRequirement requirement
    ) {
        return requirement.counts()
            .entrySet()
            .stream()
            .allMatch(
                entry -> playerProgress.buildingCounts
                             .get(entry.getKey()) >= entry.getValue()
            );
    }

    private BigDecimal bakingRate(Building building) {
        var baseBakingRate = configuration.buildingConfigurations()
            .get(building)
            .baseBakingRate();
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
                Upgrade.GRANDMA_SYNERGY_FARM,
                Upgrade.GRANDMA_SYNERGY_MINE,
                Upgrade.GRANDMA_SYNERGY_FACTORY,
                Upgrade.GRANDMA_SYNERGY_BANK,
                Upgrade.GRANDMA_SYNERGY_TEMPLE,
                Upgrade.GRANDMA_SYNERGY_WIZARD_TOWER,
                Upgrade.GRANDMA_SYNERGY_SHIPMENT,
                Upgrade.GRANDMA_SYNERGY_ALCHEMY_LAB,
                Upgrade.GRANDMA_SYNERGY_PORTAL,
                Upgrade.GRANDMA_SYNERGY_TIME_MACHINE,
                Upgrade.GRANDMA_SYNERGY_ANTIMATTER_CONDENSER,
                Upgrade.GRANDMA_SYNERGY_PRISM,
                Upgrade.GRANDMA_SYNERGY_CHANCEMAKER,
                Upgrade.GRANDMA_SYNERGY_FRACTAL_ENGINE
            );
            case FARM -> withGrandmaSynergy(
                Building.FARM,
                Upgrade.GRANDMA_SYNERGY_FARM,
                doubled(
                    baseBakingRate,
                    Upgrade.FARM_TIER_0,
                    Upgrade.FARM_TIER_1,
                    Upgrade.FARM_TIER_2,
                    Upgrade.FARM_TIER_3,
                    Upgrade.FARM_TIER_4,
                    Upgrade.FARM_TIER_5,
                    Upgrade.FARM_TIER_6,
                    Upgrade.FARM_TIER_7,
                    Upgrade.FARM_TIER_8
                )
            );
            case MINE -> withGrandmaSynergy(
                Building.MINE,
                Upgrade.GRANDMA_SYNERGY_MINE,
                doubled(
                    baseBakingRate,
                    Upgrade.MINE_TIER_0,
                    Upgrade.MINE_TIER_1,
                    Upgrade.MINE_TIER_2,
                    Upgrade.MINE_TIER_3,
                    Upgrade.MINE_TIER_4,
                    Upgrade.MINE_TIER_5,
                    Upgrade.MINE_TIER_6,
                    Upgrade.MINE_TIER_7,
                    Upgrade.MINE_TIER_8
                )
            );
            case FACTORY -> withGrandmaSynergy(
                Building.FACTORY,
                Upgrade.GRANDMA_SYNERGY_FACTORY,
                doubled(
                    baseBakingRate,
                    Upgrade.FACTORY_TIER_0,
                    Upgrade.FACTORY_TIER_1,
                    Upgrade.FACTORY_TIER_2,
                    Upgrade.FACTORY_TIER_3,
                    Upgrade.FACTORY_TIER_4,
                    Upgrade.FACTORY_TIER_5,
                    Upgrade.FACTORY_TIER_6,
                    Upgrade.FACTORY_TIER_7,
                    Upgrade.FACTORY_TIER_8
                )
            );
            case BANK -> withGrandmaSynergy(
                Building.BANK,
                Upgrade.GRANDMA_SYNERGY_BANK,
                doubled(
                    baseBakingRate,
                    Upgrade.BANK_TIER_0,
                    Upgrade.BANK_TIER_1,
                    Upgrade.BANK_TIER_2,
                    Upgrade.BANK_TIER_3,
                    Upgrade.BANK_TIER_4,
                    Upgrade.BANK_TIER_5,
                    Upgrade.BANK_TIER_6,
                    Upgrade.BANK_TIER_7,
                    Upgrade.BANK_TIER_8
                )
            );
            case TEMPLE -> withGrandmaSynergy(
                Building.TEMPLE,
                Upgrade.GRANDMA_SYNERGY_TEMPLE,
                doubled(
                    baseBakingRate,
                    Upgrade.TEMPLE_TIER_0,
                    Upgrade.TEMPLE_TIER_1,
                    Upgrade.TEMPLE_TIER_2,
                    Upgrade.TEMPLE_TIER_3,
                    Upgrade.TEMPLE_TIER_4,
                    Upgrade.TEMPLE_TIER_5,
                    Upgrade.TEMPLE_TIER_6,
                    Upgrade.TEMPLE_TIER_7,
                    Upgrade.TEMPLE_TIER_8
                )
            );
            case WIZARD_TOWER -> withGrandmaSynergy(
                Building.WIZARD_TOWER,
                Upgrade.GRANDMA_SYNERGY_WIZARD_TOWER,
                doubled(
                    baseBakingRate,
                    Upgrade.WIZARD_TOWER_TIER_0,
                    Upgrade.WIZARD_TOWER_TIER_1,
                    Upgrade.WIZARD_TOWER_TIER_2,
                    Upgrade.WIZARD_TOWER_TIER_3,
                    Upgrade.WIZARD_TOWER_TIER_4,
                    Upgrade.WIZARD_TOWER_TIER_5,
                    Upgrade.WIZARD_TOWER_TIER_6,
                    Upgrade.WIZARD_TOWER_TIER_7,
                    Upgrade.WIZARD_TOWER_TIER_8
                )
            );
            case SHIPMENT -> withGrandmaSynergy(
                Building.SHIPMENT,
                Upgrade.GRANDMA_SYNERGY_SHIPMENT,
                doubled(
                    baseBakingRate,
                    Upgrade.SHIPMENT_TIER_0,
                    Upgrade.SHIPMENT_TIER_1,
                    Upgrade.SHIPMENT_TIER_2,
                    Upgrade.SHIPMENT_TIER_3,
                    Upgrade.SHIPMENT_TIER_4,
                    Upgrade.SHIPMENT_TIER_5,
                    Upgrade.SHIPMENT_TIER_6,
                    Upgrade.SHIPMENT_TIER_7,
                    Upgrade.SHIPMENT_TIER_8
                )
            );
            case ALCHEMY_LAB -> withGrandmaSynergy(
                Building.ALCHEMY_LAB,
                Upgrade.GRANDMA_SYNERGY_ALCHEMY_LAB,
                doubled(
                    baseBakingRate,
                    Upgrade.ALCHEMY_LAB_TIER_0,
                    Upgrade.ALCHEMY_LAB_TIER_1,
                    Upgrade.ALCHEMY_LAB_TIER_2,
                    Upgrade.ALCHEMY_LAB_TIER_3,
                    Upgrade.ALCHEMY_LAB_TIER_4,
                    Upgrade.ALCHEMY_LAB_TIER_5,
                    Upgrade.ALCHEMY_LAB_TIER_6,
                    Upgrade.ALCHEMY_LAB_TIER_7,
                    Upgrade.ALCHEMY_LAB_TIER_8
                )
            );
            case PORTAL -> withGrandmaSynergy(
                Building.PORTAL,
                Upgrade.GRANDMA_SYNERGY_PORTAL,
                doubled(
                    baseBakingRate,
                    Upgrade.PORTAL_TIER_0,
                    Upgrade.PORTAL_TIER_1,
                    Upgrade.PORTAL_TIER_2,
                    Upgrade.PORTAL_TIER_3,
                    Upgrade.PORTAL_TIER_4,
                    Upgrade.PORTAL_TIER_5,
                    Upgrade.PORTAL_TIER_6,
                    Upgrade.PORTAL_TIER_7,
                    Upgrade.PORTAL_TIER_8
                )
            );
            case TIME_MACHINE -> withGrandmaSynergy(
                Building.TIME_MACHINE,
                Upgrade.GRANDMA_SYNERGY_TIME_MACHINE,
                doubled(
                    baseBakingRate,
                    Upgrade.TIME_MACHINE_TIER_0,
                    Upgrade.TIME_MACHINE_TIER_1,
                    Upgrade.TIME_MACHINE_TIER_2,
                    Upgrade.TIME_MACHINE_TIER_3,
                    Upgrade.TIME_MACHINE_TIER_4,
                    Upgrade.TIME_MACHINE_TIER_5,
                    Upgrade.TIME_MACHINE_TIER_6,
                    Upgrade.TIME_MACHINE_TIER_7,
                    Upgrade.TIME_MACHINE_TIER_8
                )
            );
            case ANTIMATTER_CONDENSER -> withGrandmaSynergy(
                Building.ANTIMATTER_CONDENSER,
                Upgrade.GRANDMA_SYNERGY_ANTIMATTER_CONDENSER,
                doubled(
                    baseBakingRate,
                    Upgrade.ANTIMATTER_CONDENSER_TIER_0,
                    Upgrade.ANTIMATTER_CONDENSER_TIER_1,
                    Upgrade.ANTIMATTER_CONDENSER_TIER_2,
                    Upgrade.ANTIMATTER_CONDENSER_TIER_3,
                    Upgrade.ANTIMATTER_CONDENSER_TIER_4,
                    Upgrade.ANTIMATTER_CONDENSER_TIER_5,
                    Upgrade.ANTIMATTER_CONDENSER_TIER_6,
                    Upgrade.ANTIMATTER_CONDENSER_TIER_7,
                    Upgrade.ANTIMATTER_CONDENSER_TIER_8
                )
            );
            case PRISM -> withGrandmaSynergy(
                Building.PRISM,
                Upgrade.GRANDMA_SYNERGY_PRISM,
                doubled(
                    baseBakingRate,
                    Upgrade.PRISM_TIER_0,
                    Upgrade.PRISM_TIER_1,
                    Upgrade.PRISM_TIER_2,
                    Upgrade.PRISM_TIER_3,
                    Upgrade.PRISM_TIER_4,
                    Upgrade.PRISM_TIER_5,
                    Upgrade.PRISM_TIER_6,
                    Upgrade.PRISM_TIER_7,
                    Upgrade.PRISM_TIER_8
                )
            );
            case CHANCEMAKER -> withGrandmaSynergy(
                Building.CHANCEMAKER,
                Upgrade.GRANDMA_SYNERGY_CHANCEMAKER,
                doubled(
                    baseBakingRate,
                    Upgrade.CHANCEMAKER_TIER_0,
                    Upgrade.CHANCEMAKER_TIER_1,
                    Upgrade.CHANCEMAKER_TIER_2,
                    Upgrade.CHANCEMAKER_TIER_3,
                    Upgrade.CHANCEMAKER_TIER_4,
                    Upgrade.CHANCEMAKER_TIER_5,
                    Upgrade.CHANCEMAKER_TIER_6,
                    Upgrade.CHANCEMAKER_TIER_7,
                    Upgrade.CHANCEMAKER_TIER_8
                )
            );
            case FRACTAL_ENGINE -> withGrandmaSynergy(
                Building.FRACTAL_ENGINE,
                Upgrade.GRANDMA_SYNERGY_FRACTAL_ENGINE,
                doubled(
                    baseBakingRate,
                    Upgrade.FRACTAL_ENGINE_TIER_0,
                    Upgrade.FRACTAL_ENGINE_TIER_1,
                    Upgrade.FRACTAL_ENGINE_TIER_2,
                    Upgrade.FRACTAL_ENGINE_TIER_3,
                    Upgrade.FRACTAL_ENGINE_TIER_4,
                    Upgrade.FRACTAL_ENGINE_TIER_5,
                    Upgrade.FRACTAL_ENGINE_TIER_6,
                    Upgrade.FRACTAL_ENGINE_TIER_7,
                    Upgrade.FRACTAL_ENGINE_TIER_8
                )
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
        if (!isActive(Upgrade.CURSOR_TIER_3)) {
            return BigDecimal.ZERO;
        }
        return multiplied(
            multiplied(
                multiplied(
                    BigDecimal.valueOf(0.1)
                        .multiply(new BigDecimal(nonCursorBuildingCount())),
                    new BigDecimal(5),
                    Upgrade.CURSOR_TIER_4
                ),
                new BigDecimal(10),
                Upgrade.CURSOR_TIER_5
            ),
            new BigDecimal(20),
            Upgrade.CURSOR_TIER_6,
            Upgrade.CURSOR_TIER_7,
            Upgrade.CURSOR_TIER_8
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
            if (isActive(upgrade)) {
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

    private BigDecimal withGrandmaSynergy(
        Building building,
        Upgrade upgrade,
        BigDecimal bakingRate
    ) {
        if (!isActive(upgrade)) {
            return bakingRate;
        }
        var multiplier = count(Building.GRANDMA) /
                         (building.ordinal() - Building.GRANDMA.ordinal());
        return bakingRate.multiply(BigDecimal.valueOf(1 + 0.01 * multiplier));
    }

    private BigDecimal withMilk(BigDecimal base) {
        var multipliers = Map.of(
            Upgrade.KITTEN_TIER_0, 0.1,
            Upgrade.KITTEN_TIER_1, 0.125,
            Upgrade.KITTEN_TIER_2, 0.15,
            Upgrade.KITTEN_TIER_3, 0.175,
            Upgrade.KITTEN_TIER_4, 0.2
        );
        var bakingRate = base;
        var milk = BigDecimal.valueOf(milk());
        for (var entry : multipliers.entrySet()) {
            if (isActive(entry.getKey())) {
                bakingRate = bakingRate.multiply(
                    BigDecimal.ONE.add(
                        milk.multiply(
                            BigDecimal.valueOf(entry.getValue())
                        )
                    )
                );
            }
        }
        return bakingRate;
    }
}
