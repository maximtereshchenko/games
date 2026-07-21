package com.github.maximtereshchenko.games.snakes.session;

import java.util.Objects;

final class Timer {

    final int turns;
    int turnsRemaining;

    Timer(int turns) {
        this.turns = turns;
        this.turnsRemaining = turns;
    }

    @Override
    public int hashCode() {
        return Objects.hash(turns, turnsRemaining);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof Timer timer &&
               turns == timer.turns &&
               turnsRemaining == timer.turnsRemaining;
    }
}
