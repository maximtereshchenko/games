package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class PlayerProgress {

    private final Map<Building, Integer> buildings;
    private final Set<Upgrade> unlockedUpgrades;
    private final Set<Upgrade> activeUpgrades;
    private final Instant timestamp;
    private BigDecimal balance;
    private BigDecimal cumulativeBaked;
    private BigDecimal cumulativeManuallyBaked;
    private long cumulativeClicks;

    PlayerProgress(Clock clock) {
        this.buildings = new EnumMap<>(Building.class);
        this.unlockedUpgrades = new HashSet<>();
        this.activeUpgrades = new HashSet<>();
        this.timestamp = Instant.now(clock);
        this.balance = BigDecimal.ZERO;
        this.cumulativeBaked = BigDecimal.ZERO;
        this.cumulativeManuallyBaked = BigDecimal.ZERO;
        this.cumulativeClicks = 0;
        for (var building : Building.values()) {
            buildings.put(building, 0);
        }
    }

    Map<Building, Integer> buildings() {
        return buildings;
    }

    Set<Upgrade> unlockedUpgrades() {
        return unlockedUpgrades;
    }

    Set<Upgrade> activeUpgrades() {
        return activeUpgrades;
    }

    Instant timestamp() {
        return timestamp;
    }

    BigDecimal balance() {
        return balance;
    }

    void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    BigDecimal cumulativeBaked() {
        return cumulativeBaked;
    }

    void setCumulativeBaked(BigDecimal cumulativeBaked) {
        this.cumulativeBaked = cumulativeBaked;
    }

    BigDecimal cumulativeManuallyBaked() {
        return cumulativeManuallyBaked;
    }

    void setCumulativeManuallyBaked(BigDecimal cumulativeManuallyBaked) {
        this.cumulativeManuallyBaked = cumulativeManuallyBaked;
    }

    long cumulativeClicks() {
        return cumulativeClicks;
    }

    void setCumulativeClicks(long cumulativeClicks) {
        this.cumulativeClicks = cumulativeClicks;
    }
}
