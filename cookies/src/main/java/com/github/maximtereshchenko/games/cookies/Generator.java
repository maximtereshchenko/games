package com.github.maximtereshchenko.games.cookies;

import java.math.BigDecimal;

enum Generator {

    CURSOR(BigDecimal.valueOf(15), BigDecimal.valueOf(0.1));

    private final BigDecimal baseCost;
    private final BigDecimal baseProduction;

    Generator(BigDecimal baseCost, BigDecimal baseProduction) {
        this.baseCost = baseCost;
        this.baseProduction = baseProduction;
    }

    BigDecimal baseCost() {
        return baseCost;
    }

    BigDecimal baseProduction() {
        return baseProduction;
    }
}
