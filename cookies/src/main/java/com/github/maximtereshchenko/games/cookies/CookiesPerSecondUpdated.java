package com.github.maximtereshchenko.games.cookies;

import java.math.BigDecimal;

record CookiesPerSecondUpdated(BigDecimal value) implements Event {}
