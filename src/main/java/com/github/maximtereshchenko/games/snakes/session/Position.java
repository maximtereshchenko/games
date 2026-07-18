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
            case UP -> y = adjusted(y + 1, worldDimensions.height());
            case DOWN -> y = adjusted(y - 1, worldDimensions.height());
            case LEFT -> x = adjusted(x - 1, worldDimensions.width());
            case RIGHT -> x = adjusted(x + 1, worldDimensions.width());
        }
    }

    private int adjusted(int value, int max) {
        return (value + max) % max;
    }
}
