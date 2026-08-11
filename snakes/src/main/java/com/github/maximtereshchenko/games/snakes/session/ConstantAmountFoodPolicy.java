package com.github.maximtereshchenko.games.snakes.session;

record ConstantAmountFoodPolicy(
    int periodTurns,
    Direction direction,
    int max,
    float growthStep
) {}
