package com.github.maximtereshchenko.games.snakes.session;

import java.util.Objects;

final class SidewaysDirection {

    final int cycle;
    int index;

    SidewaysDirection(int cycle, int index) {
        this.cycle = cycle;
        this.index = index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cycle, index);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof SidewaysDirection that &&
               cycle == that.cycle &&
               index == that.index;
    }
}
