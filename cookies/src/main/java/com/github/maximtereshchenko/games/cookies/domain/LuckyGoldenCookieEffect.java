package com.github.maximtereshchenko.games.cookies.domain;

import java.math.BigDecimal;

public record LuckyGoldenCookieEffect(BigDecimal amount)
    implements GoldenCookieEffect {}
