package com.github.maximtereshchenko.games.snakes.session;

final class Timer {

    final int period;
    int turnsRemaining;

    Timer(int period, int turnsRemaining) {
        this.period = period;
        this.turnsRemaining = turnsRemaining;
    }
}
