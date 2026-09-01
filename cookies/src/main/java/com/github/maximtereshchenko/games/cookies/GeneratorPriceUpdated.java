package com.github.maximtereshchenko.games.cookies;

import java.math.BigDecimal;

record GeneratorPriceUpdated(Generator generator, BigDecimal price) implements Event {}
