package com.github.maximtereshchenko.games.cookies.domain;

public sealed interface Event permits CookieAmountUpdated,
    CookiesClicked,
    CookiesPerSecondUpdated,
    GeneratorBought,
    GeneratorPriceUpdated,
    GeneratorUnlocked {}
