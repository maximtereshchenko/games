package com.github.maximtereshchenko.games.snake;

final class Head {

    Direction direction;

    Head(Direction direction) {
        this.direction = direction;
    }

    enum Direction {

        UP, DOWN, LEFT, RIGHT
    }
}
