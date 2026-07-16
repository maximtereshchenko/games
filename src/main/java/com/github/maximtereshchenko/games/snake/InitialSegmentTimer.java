package com.github.maximtereshchenko.games.snake;

import java.util.Objects;

final class InitialSegmentTimer {

    int value;

    InitialSegmentTimer(int value) {
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
        return object instanceof InitialSegmentTimer that &&
               value == that.value;
    }
}
