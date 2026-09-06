package com.github.maximtereshchenko.games.cookies.domain;

final class BuildingCountRequirement implements UnlockRequirement {

    private final Building building;
    private final int count;

    BuildingCountRequirement(Building building, int count) {
        this.building = building;
        this.count = count;
    }

    @Override
    public boolean isSatisfied(PlayerProgress playerProgress) {
        return playerProgress.buildings()
                   .get(building) >= count;
    }
}
