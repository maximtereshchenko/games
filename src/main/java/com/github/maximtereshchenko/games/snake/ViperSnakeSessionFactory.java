package com.github.maximtereshchenko.games.snake;

final class ViperSnakeSessionFactory extends SnakeSessionFactory {

    @Override
    Mode mode() {
        return Mode.VIPER;
    }

    @Override
    boolean setCurrentDirection(Direction current, Direction next) {
        return next == current.right();
    }
}
