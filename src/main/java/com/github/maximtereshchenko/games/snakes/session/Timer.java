package com.github.maximtereshchenko.games.snakes.session;

import java.util.Objects;

final class Timer {

    final int turns;
    int turnsLeft;

    Timer(int turns) {
        this.turns = turns;
        this.turnsLeft = turns;
    }

    @Override
    public int hashCode() {
        return Objects.hash(turns, turnsLeft);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof Timer timer &&
               turns == timer.turns &&
               turnsLeft == timer.turnsLeft;
    }
}
