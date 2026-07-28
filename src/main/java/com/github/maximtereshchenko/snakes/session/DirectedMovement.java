package com.github.maximtereshchenko.snakes.session;

final class DirectedMovement {

    final int periodTurns;
    int remainingTurns;

    DirectedMovement(int periodTurns, int remainingTurns) {
        this.periodTurns = periodTurns;
        this.remainingTurns = remainingTurns;
    }
}
