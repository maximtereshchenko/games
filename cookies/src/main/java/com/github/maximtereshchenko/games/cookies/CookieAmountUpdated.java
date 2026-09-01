package com.github.maximtereshchenko.games.cookies;

import java.math.BigDecimal;

record CookieAmountUpdated(BigDecimal value) implements Event {}
