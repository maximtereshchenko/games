package com.github.maximtereshchenko.games.cookies.domain;

record AchievementCountUnlockRequirement(int count)
    implements UpgradeUnlockRequirement {}
