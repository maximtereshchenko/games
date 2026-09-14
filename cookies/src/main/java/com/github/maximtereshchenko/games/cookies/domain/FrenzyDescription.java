package com.github.maximtereshchenko.games.cookies.domain;

public record FrenzyDescription(
    float multiplier,
    float durationSeconds
) implements BuffDescription {}
