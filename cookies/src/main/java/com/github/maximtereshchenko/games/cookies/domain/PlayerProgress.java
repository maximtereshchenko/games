package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class PlayerProgress {

    private final Set<Building> unlockedBuildings;
    private final Map<Building, Integer> buildings;
    private BigDecimal balance;

    PlayerProgress() {
        this.unlockedBuildings = new HashSet<>();
        this.buildings = new EnumMap<>(Building.class);
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

    int count(Building building) {
        return buildings.get(building);
    }

    void add(Building building, int count) {
        buildings.computeIfPresent(
            building,
            (_, current) -> current + count
        );
    }
}
