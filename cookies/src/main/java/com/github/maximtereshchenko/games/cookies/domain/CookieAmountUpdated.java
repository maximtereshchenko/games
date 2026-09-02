package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record CookieAmountUpdated(BigDecimal value) implements Event {}
