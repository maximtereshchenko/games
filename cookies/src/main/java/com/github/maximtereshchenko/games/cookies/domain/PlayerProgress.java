package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class PlayerProgress {

    final Map<Building, Integer> buildings;
    final Set<Upgrade> unlockedUpgrades;
    final Set<Upgrade> activeUpgrades;
    final Set<Achievement> unlockedAchievements;
    final Instant timestamp;
    BigDecimal balance;
    BigDecimal cumulativeBaked;
    BigDecimal cumulativeManuallyBaked;
    long cumulativeClicks;

    PlayerProgress(Clock clock) {
        this.buildings = new EnumMap<>(Building.class);
        this.unlockedUpgrades = new HashSet<>();
        this.activeUpgrades = new HashSet<>();
        this.unlockedAchievements = new HashSet<>();
        this.timestamp = Instant.now(clock);
        this.balance = BigDecimal.ZERO;
        this.cumulativeBaked = BigDecimal.ZERO;
        this.cumulativeManuallyBaked = BigDecimal.ZERO;
        this.cumulativeClicks = 0;
        for (var building : Building.values()) {
            buildings.put(building, 0);
        }
    }
}
