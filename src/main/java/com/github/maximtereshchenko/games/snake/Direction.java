package com.github.maximtereshchenko.games.snake;

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
