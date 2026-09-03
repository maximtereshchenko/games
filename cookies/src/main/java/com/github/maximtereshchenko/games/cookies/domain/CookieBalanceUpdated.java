package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record CookieBalanceUpdated(BigDecimal value) implements Event {}
