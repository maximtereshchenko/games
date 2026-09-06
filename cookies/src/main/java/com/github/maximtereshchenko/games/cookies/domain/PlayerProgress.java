package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class PlayerProgress {

    private final Map<Building, Integer> buildings;
    private final Set<Upgrade> unlockedUpgrades;
    private final Set<Upgrade> activeUpgrades;
    private BigDecimal balance;

    PlayerProgress() {
        this.buildings = new EnumMap<>(Building.class);
        this.unlockedUpgrades = new HashSet<>();
        this.activeUpgrades = new HashSet<>();
        this.balance = BigDecimal.ZERO;
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

    BigDecimal balance() {
        return balance;
    }

    void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
