package com.github.maximtereshchenko.games.snake;

enum Direction {

    UP(2), DOWN(3), LEFT(1), RIGHT(0);

    private final int leftOrdinal;

    Direction(int leftOrdinal) {
        this.leftOrdinal = leftOrdinal;
    }

    Direction opposite() {
        return left().left();
    }

    Direction left() {
        return Direction.values()[leftOrdinal];
    }

    Direction right() {
        return opposite().left();
    }

    Direction after(LegalTurn legalTurn) {
        return switch (legalTurn) {
            case LEFT -> left();
            case RIGHT -> right();
        };
    }
}
