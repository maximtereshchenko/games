package com.github.maximtereshchenko.games.snakes.session;

final class SidewaysMovement {

    final int periodTurns;
    final int cycle;
    int remainingTurns;
    int index;

    SidewaysMovement(int periodTurns, int cycle, int remainingTurns, int index) {
        this.periodTurns = periodTurns;
        this.cycle = cycle;
        this.remainingTurns = remainingTurns;
        this.index = index;
    }
}
