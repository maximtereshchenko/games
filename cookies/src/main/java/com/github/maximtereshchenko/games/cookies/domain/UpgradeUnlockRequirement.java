package com.github.maximtereshchenko.games.cookies.domain;

sealed interface UpgradeUnlockRequirement extends UnlockRequirement
    permits BuildingCountUnlockRequirement,
    ManuallyBakedUnlockRequirement,
    TieredUnlockRequirement {}
