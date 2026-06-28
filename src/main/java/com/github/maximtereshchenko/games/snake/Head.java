package com.github.maximtereshchenko.games.snake;

final class Head {

    Direction direction;

    Head(Direction direction) {
        this.direction = direction;
    }

    enum Direction {

        UP(1), DOWN(0), LEFT(3), RIGHT(2);

        private final int oppositeOrdinal;

        Direction(int oppositeOrdinal) {
            this.oppositeOrdinal = oppositeOrdinal;
        }

        Direction opposite() {
            return Direction.values()[oppositeOrdinal];
        }
    }
}
