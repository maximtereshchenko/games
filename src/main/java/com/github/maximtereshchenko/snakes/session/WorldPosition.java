package com.github.maximtereshchenko.snakes.session;

import java.util.Objects;

class WorldPosition {

    int x;
    int y;

    WorldPosition() {}

    WorldPosition(int x, int y) {
        this.x = x;
        this.y = y;
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
        return object instanceof WorldPosition worldPosition &&
               x == worldPosition.x &&
               y == worldPosition.y;
    }

    void move(Direction direction) {
        switch (direction) {
            case UP -> y++;
            case DOWN -> y--;
            case LEFT -> x--;
            case RIGHT -> x++;
        }
    }

    void copy(WorldPosition position) {
        x = position.x;
        y = position.y;
    }
}
