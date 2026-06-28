package com.github.maximtereshchenko.games.snake;

import java.util.Objects;

final class Position {

    int x;
    int y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Position(Position position) {
        this(position.x, position.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof Position position &&
               x == position.x &&
               y == position.y;
    }
}
