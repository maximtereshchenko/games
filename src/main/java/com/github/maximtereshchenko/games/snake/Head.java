package com.github.maximtereshchenko.games.snake;

final class Head {

    Direction current;
    Direction next;

    Head(Direction current, Direction next) {
        this.current = current;
        this.next = next;
    }

    Head(Direction current) {
        this(current, current);
    }

    Head() {
        this(Head.Direction.RIGHT);
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
