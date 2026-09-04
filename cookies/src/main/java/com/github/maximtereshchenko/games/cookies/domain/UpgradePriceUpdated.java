package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record UpgradePriceUpdated(Upgrade upgrade, BigDecimal price) implements Event {}
