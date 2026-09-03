package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record BakingRateUpdated(BigDecimal value) implements Event {}
