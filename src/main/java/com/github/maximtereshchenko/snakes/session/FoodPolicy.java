package com.github.maximtereshchenko.snakes.session;

record FoodPolicy(
    WorldDimensions worldDimensions,
    int periodTurns,
    Direction direction,
    int max
) {}
