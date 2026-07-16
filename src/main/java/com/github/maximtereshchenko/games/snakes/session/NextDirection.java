package com.github.maximtereshchenko.games.snakes.session;

import java.util.Objects;

final class NextDirection {

    Direction value;

    NextDirection(Direction value) {
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
        return object instanceof NextDirection that &&
               value == that.value;
    }
}
