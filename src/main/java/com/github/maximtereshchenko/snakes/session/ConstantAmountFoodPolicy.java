package com.github.maximtereshchenko.snakes.session;

record ConstantAmountFoodPolicy(
    int periodTurns,
    Direction direction,
    int max,
    float growthStep //TODO
) {}
