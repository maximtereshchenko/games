package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record TransactionValueUpdated(Building building, BigDecimal value) implements Event {}
