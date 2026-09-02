package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record CookiesPerSecondUpdated(BigDecimal value) implements Event {}
