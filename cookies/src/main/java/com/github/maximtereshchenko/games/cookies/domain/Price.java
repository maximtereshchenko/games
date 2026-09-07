package com.github.maximtereshchenko.games.cookies.domain;

sealed interface Price permits ExactPrice, TieredPrice {}
