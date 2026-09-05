package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class PlayerProgress {

    private final Set<Building> unlockedBuildings;
    private final Map<Building, Integer> buildings;
    private final Set<Upgrade> unlockedUpgrades;
    private final Set<Upgrade> activeUpgrades;
    private BigDecimal balance;

    PlayerProgress() {
        this.unlockedBuildings = new HashSet<>();
        this.buildings = new EnumMap<>(Building.class);
        this.unlockedUpgrades = new HashSet<>();
        this.activeUpgrades = new HashSet<>();
        this.balance = BigDecimal.ZERO;
        for (var building : Building.values()) {
            buildings.put(building, 0);
        }
    }

    BigDecimal balance() {
        return balance;
    }

    void addToBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }

    void unlock(Building building) {
        unlockedBuildings.add(building);
    }

    boolean isUnlocked(Building building) {
        return unlockedBuildings.contains(building);
    }

    void unlock(Upgrade upgrade) {
        unlockedUpgrades.add(upgrade);
    }

    boolean isUnlocked(Upgrade upgrade) {
        return unlockedUpgrades.contains(upgrade);
    }

    int count(Building building) {
        return buildings.get(building);
    }

    void add(Building building, int count) {
        buildings.computeIfPresent(
            building,
            (_, current) -> current + count
        );
    }

    void activate(Upgrade upgrade) {
        activeUpgrades.add(upgrade);
    }

    boolean isActive(Upgrade upgrade) {
        return activeUpgrades.contains(upgrade);
    }
}
