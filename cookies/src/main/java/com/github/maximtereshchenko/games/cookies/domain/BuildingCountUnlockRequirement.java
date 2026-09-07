package com.github.maximtereshchenko.games.cookies.domain;

record BuildingCountUnlockRequirement(
    Building building,
    int count
) implements UnlockRequirement {}
