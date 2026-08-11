package com.github.maximtereshchenko.games.snakes.session;

record TurnLengthScaling(
    float baseTurnLengthSeconds,
    int foodConsumedStep,
    float turnLengthReductionSeconds,
    float minimalTurnLengthSeconds
) {}
