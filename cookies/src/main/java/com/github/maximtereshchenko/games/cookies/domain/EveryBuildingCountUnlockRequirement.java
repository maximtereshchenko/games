package com.github.maximtereshchenko.games.cookies.domain;

record EveryBuildingCountUnlockRequirement(
    int count
) implements AchievementUnlockRequirement {}
