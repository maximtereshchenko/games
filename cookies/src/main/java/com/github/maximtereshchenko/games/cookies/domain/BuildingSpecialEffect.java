package com.github.maximtereshchenko.games.cookies.domain;

public record BuildingSpecialEffect(
    Building building,
    int count,
    float multiplier
) implements BuffEffect {}
