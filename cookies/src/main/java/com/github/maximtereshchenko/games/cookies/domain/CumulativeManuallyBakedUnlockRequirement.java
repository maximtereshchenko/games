package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

record CumulativeManuallyBakedUnlockRequirement(BigDecimal value)
    implements AchievementUnlockRequirement {}
