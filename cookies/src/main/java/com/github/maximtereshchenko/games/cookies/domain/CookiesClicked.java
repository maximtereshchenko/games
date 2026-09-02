package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record CookiesClicked(BigDecimal value) implements Event {}
