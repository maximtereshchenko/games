package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

record ManuallyBakedUnlockRequirement(BigDecimal count)
    implements UnlockRequirement {}
