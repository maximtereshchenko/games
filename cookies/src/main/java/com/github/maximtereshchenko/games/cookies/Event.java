package com.github.maximtereshchenko.games.cookies;

sealed interface Event permits CookieAmountUpdated, CookiesClicked, GeneratorBought, GeneratorPriceUpdated, GeneratorUnlocked {}
