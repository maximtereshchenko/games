package com.github.maximtereshchenko.snakes.session;

record TurnLengthScaling(
    float baseTurnLengthSeconds,
    int foodConsumedStep,
    float turnLengthReductionSeconds,
    float minimalTurnLengthSeconds
) {}
