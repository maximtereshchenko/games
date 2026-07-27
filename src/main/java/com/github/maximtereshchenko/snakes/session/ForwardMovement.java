package com.github.maximtereshchenko.snakes.session;

final class ForwardMovement {

    final int periodTurns;
    int remainingTurns;
    Direction direction;

    ForwardMovement(int periodTurns, int remainingTurns, Direction direction) {
        this.periodTurns = periodTurns;
        this.remainingTurns = remainingTurns;
        this.direction = direction;
    }
}
