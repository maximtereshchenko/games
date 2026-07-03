package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

final class SnakesGame extends Game implements Subscriber {

    private final ShapeRenderer shapeRenderer;
    private final SnakeSessionScreen snakeSessionScreen;

    SnakesGame(ShapeRenderer shapeRenderer, SnakeSessionScreen snakeSessionScreen) {
        this.shapeRenderer = shapeRenderer;
        this.snakeSessionScreen = snakeSessionScreen;
        setScreen(snakeSessionScreen);
    }

    @Override
    public void create() {
        //empty
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void onEvent(ApplicationEvent event) {
        switch (event) {
            case SNAKE_SESSION_ENDED -> setScreen(snakeSessionScreen);
        }
    }
}
