package com.github.maximtereshchenko.games.snakes.session;

import java.util.Objects;

final class Timer {

    int value;

    Timer(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof Timer timer &&
               value == timer.value;
    }
}
