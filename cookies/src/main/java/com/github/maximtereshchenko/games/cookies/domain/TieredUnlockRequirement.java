package com.github.maximtereshchenko.games.cookies.domain;

record TieredUnlockRequirement(
    Building building,
    int tier
) implements UpgradeUnlockRequirement {}
