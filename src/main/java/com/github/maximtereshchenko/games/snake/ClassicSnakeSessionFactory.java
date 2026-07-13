package com.github.maximtereshchenko.games.snake;

final class ClassicSnakeSessionFactory extends SnakeSessionFactory {

    @Override
    Mode mode() {
        return Mode.CLASSIC;
    }

    @Override
    boolean setCurrentDirection(Direction current, Direction next) {
        return current.opposite() != next;
    }
}
