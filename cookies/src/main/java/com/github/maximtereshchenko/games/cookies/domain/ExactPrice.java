package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

record ExactPrice(BigDecimal value) implements Price {}
