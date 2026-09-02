package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record GeneratorPriceUpdated(Building generator, BigDecimal price) implements Event {}
