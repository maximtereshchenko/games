package com.github.maximtereshchenko.games.cookies.domain;

record TotalBuildingCountUnlockRequirement(
    int count
) implements AchievementUnlockRequirement {}
