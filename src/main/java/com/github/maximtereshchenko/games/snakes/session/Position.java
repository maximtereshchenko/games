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

    void move(WorldDimensions worldDimensions, Direction direction) {
        switch (direction) {
            case UP -> y = (y + 1) % worldDimensions.height();
            case DOWN -> y = (y - 1 + worldDimensions.height()) % worldDimensions.height();
            case LEFT -> x = (x - 1 + worldDimensions.width()) % worldDimensions.width();
            case RIGHT -> x = (x + 1) % worldDimensions.width();
        }
    }
}
