package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.function.Supplier;

final class SnakeGame extends Game {

    private final ScreenFactory screenFactory;
    private final Supplier<ShapeRenderer> supplier;
    private ShapeRenderer shapeRenderer;

    SnakeGame(ScreenFactory screenFactory, Supplier<ShapeRenderer> supplier) {
        this.screenFactory = screenFactory;
        this.supplier = supplier;
    }

    @Override
    public void create() {
        this.shapeRenderer = supplier.get();
        startSnakeSession();
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }

    private void startSnakeSession() {
        setScreen(screenFactory.snakeSessionScreen(shapeRenderer, this::startSnakeSession));
    }
}
