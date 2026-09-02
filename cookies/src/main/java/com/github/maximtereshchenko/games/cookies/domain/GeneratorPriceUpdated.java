package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record GeneratorPriceUpdated(Generator generator, BigDecimal price) implements Event {}
