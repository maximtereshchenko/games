package com.github.maximtereshchenko.games.cookies.domain;

record ManuallyBakedUnlockRequirement(float percent)
    implements UpgradeUnlockRequirement {}
