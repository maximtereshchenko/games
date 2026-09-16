package com.github.maximtereshchenko.games.cookies.domain;

import java.util.Map;

record BuildingCountUnlockRequirement(
    Map<Building, Integer> counts
) implements UpgradeUnlockRequirement {}
