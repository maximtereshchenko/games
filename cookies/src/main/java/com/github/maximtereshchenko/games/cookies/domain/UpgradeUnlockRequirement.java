package com.github.maximtereshchenko.games.cookies.domain;

sealed interface UpgradeUnlockRequirement extends UnlockRequirement
    permits AchievementCountUnlockRequirement,
    BuildingCountUnlockRequirement,
    ManuallyBakedUnlockRequirement,
    TieredUnlockRequirement,
    GoldenCookieCountRequirement {}
