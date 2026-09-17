package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

record BakingRateUnlockRequirement(BigDecimal value)
    implements AchievementUnlockRequirement {}
