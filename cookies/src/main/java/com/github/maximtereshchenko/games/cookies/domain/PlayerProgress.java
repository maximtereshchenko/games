package com.github.maximtereshchenko.games.cookies.domain;

import com.badlogic.gdx.Preferences;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class PlayerProgress {

    private static final String BUILDING_COUNT_KEY = "buildings.%s.count";
    private static final String UPGRADE_UNLOCKED_KEY = "upgrades.%s.unlocked";
    private static final String UPGRADE_ACTIVE_KEY = "upgrades.%s.active";
    private static final String ACHIEVEMENT_UNLOCKED_KEY = "achievements.%s.unlocked";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String BALANCE_KEY = "balance";
    private static final String CUMULATIVE_BAKED_KEY = "cumulative-baked";
    private static final String CUMULATIVE_MANUALLY_BAKED_KEY = "cumulative-manually-baked";
    private static final String CUMULATIVE_CLICKS_KEY = "cumulative-clicks";

    final Map<Building, Integer> buildingCounts;
    final Set<Upgrade> unlockedUpgrades;
    final Set<Upgrade> activeUpgrades;
    final Set<Achievement> unlockedAchievements;
    final Instant timestamp;
    private final Preferences preferences;
    BigDecimal balance;
    BigDecimal cumulativeBaked;
    BigDecimal cumulativeManuallyBaked;
    long cumulativeClicks;

    public PlayerProgress(Preferences preferences, Clock clock) {
        this.preferences = preferences;
        this.buildingCounts = new EnumMap<>(Building.class);
        this.unlockedUpgrades = new HashSet<>();
        this.activeUpgrades = new HashSet<>();
        this.unlockedAchievements = new HashSet<>();
        this.timestamp = timestamp(clock);
        this.balance = bigDecimal(BALANCE_KEY);
        this.cumulativeBaked = bigDecimal(
            CUMULATIVE_BAKED_KEY
        );
        this.cumulativeManuallyBaked = bigDecimal(
            CUMULATIVE_MANUALLY_BAKED_KEY
        );
        this.cumulativeClicks = preferences.getLong(
            CUMULATIVE_CLICKS_KEY
        );
        readBuildingCounts();
        read(
            Upgrade.values(),
            UPGRADE_UNLOCKED_KEY,
            unlockedUpgrades
        );
        read(
            Upgrade.values(),
            UPGRADE_ACTIVE_KEY,
            activeUpgrades
        );
        read(
            Achievement.values(),
            ACHIEVEMENT_UNLOCKED_KEY,
            unlockedAchievements
        );
    }

    public void flush() {
        for (var building : Building.values()) {
            preferences.putInteger(
                BUILDING_COUNT_KEY.formatted(building),
                buildingCounts.get(building)
            );
        }
        putTrue(unlockedUpgrades, UPGRADE_UNLOCKED_KEY);
        putTrue(activeUpgrades, UPGRADE_ACTIVE_KEY);
        putTrue(unlockedAchievements, ACHIEVEMENT_UNLOCKED_KEY);
        preferences.putString(
            TIMESTAMP_KEY,
            timestamp.toString()
        );
        preferences.putString(
            BALANCE_KEY,
            balance.toString()
        );
        preferences.putString(
            CUMULATIVE_BAKED_KEY,
            cumulativeBaked.toString()
        );
        preferences.putString(
            CUMULATIVE_MANUALLY_BAKED_KEY,
            cumulativeManuallyBaked.toString()
        );
        preferences.putLong(
            CUMULATIVE_CLICKS_KEY,
            cumulativeClicks
        );
        preferences.flush();
    }

    private <T> void putTrue(Set<T> values, String key) {
        for (var value : values) {
            preferences.putBoolean(
                key.formatted(value),
                true
            );
        }
    }

    private BigDecimal bigDecimal(
        String key
    ) {
        if (preferences.contains(key)) {
            return new BigDecimal(preferences.getString(key));
        }
        return BigDecimal.ZERO;
    }

    private Instant timestamp(
        Clock clock
    ) {
        if (preferences.contains(TIMESTAMP_KEY)) {
            return Instant.parse(preferences.getString(TIMESTAMP_KEY));
        }
        return Instant.now(clock);
    }

    private void readBuildingCounts() {
        for (var building : Building.values()) {
            buildingCounts.put(
                building,
                preferences.getInteger(
                    BUILDING_COUNT_KEY.formatted(building)
                )
            );
        }
    }

    private <T> void read(
        T[] values,
        String pattern,
        Set<T> set
    ) {
        for (var value : values) {
            if (preferences.getBoolean(pattern.formatted(value))) {
                set.add(value);
            }
        }
    }
}
