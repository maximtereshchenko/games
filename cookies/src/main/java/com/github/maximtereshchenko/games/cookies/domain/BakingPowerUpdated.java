package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record BakingPowerUpdated(BigDecimal value) implements Event {}
