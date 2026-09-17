package com.github.maximtereshchenko.games.cookies.domain;

record TotalUpgradeCountUnlockRequirement(
    int count
) implements AchievementUnlockRequirement {}
