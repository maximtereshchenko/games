package com.github.maximtereshchenko.games.snakes.session;

import java.util.Objects;

public final class Position {

    int x;
    int y;

    public Position(int x, int y) {
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

    void move(Direction direction) {
        switch (direction) {
            case UP -> y++;
            case DOWN -> y--;
            case LEFT -> x--;
            case RIGHT -> x++;
        }
    }
}
