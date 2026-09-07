package com.github.maximtereshchenko.games.cookies.domain;

record TieredPrice(Building building, int tier) implements Price {}
