package com.github.maximtereshchenko.games.snake;

import java.util.Objects;

final class CurrentDirection {

    Direction value;

    CurrentDirection(Direction value) {
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
        return object instanceof CurrentDirection that &&
               value == that.value;
    }
}
