package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

final class SnakesGameAdapter implements ApplicationListener {

    private ApplicationListener original;

    static void main() {
        new Lwjgl3Application(new SnakesGameAdapter());
    }

    @Override
    public void create() {
        var worldDimensions = new WorldDimensions(6, 6);
        var applicationEvents = new ApplicationEvents();
        var shapeRenderer = new ShapeRenderer();
        var snakesGame = new SnakesGame(
            shapeRenderer,
            new SnakeSessionScreen(
                new SnakeSessionFactory(),
                worldDimensions,
                shapeRenderer,
                new FitViewport(worldDimensions.width(), worldDimensions.height()),
                applicationEvents
            )
        );
        applicationEvents.subscribe(snakesGame);
        original = snakesGame;
    }

    @Override
    public void resize(int width, int height) {
        original.resize(width, height);
    }

    @Override
    public void render() {
        original.render();
    }

    @Override
    public void pause() {
        original.pause();
    }

    @Override
    public void resume() {
        original.resume();
    }

    @Override
    public void dispose() {
        original.dispose();
    }
}
