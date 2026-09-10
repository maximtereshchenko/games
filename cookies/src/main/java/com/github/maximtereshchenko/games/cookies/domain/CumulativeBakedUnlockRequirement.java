package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

record CumulativeBakedUnlockRequirement(BigDecimal value)
    implements AchievementUnlockRequirement {}
