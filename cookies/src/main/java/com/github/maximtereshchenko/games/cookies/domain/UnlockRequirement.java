package com.github.maximtereshchenko.games.cookies.domain;

sealed interface UnlockRequirement
    permits BuildingCountUnlockRequirement, ManuallyBakedUnlockRequirement, TieredUnlockRequirement {}
