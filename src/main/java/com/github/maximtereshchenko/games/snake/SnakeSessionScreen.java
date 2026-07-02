package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.concurrent.TimeUnit;

final class SnakeSessionScreen extends ScreenAdapter {

    private final SnakeSessionFactory snakeSessionFactory;
    private final WorldDimensions worldDimensions;
    private final ShapeRenderer shapeRenderer;
    private final FitViewport fitViewport;
    private final Runnable onSessionEnd;
    private SnakeSession snakeSession;

    SnakeSessionScreen(
        SnakeSessionFactory snakeSessionFactory,
        WorldDimensions worldDimensions,
        ShapeRenderer shapeRenderer,
        FitViewport fitViewport,
        Runnable onSessionEnd
    ) {
        this.snakeSessionFactory = snakeSessionFactory;
        this.worldDimensions = worldDimensions;
        this.shapeRenderer = shapeRenderer;
        this.fitViewport = fitViewport;
        this.onSessionEnd = onSessionEnd;
    }

    SnakeSessionScreen(
        SnakeSessionFactory snakeSessionFactory,
        WorldDimensions worldDimensions,
        ShapeRenderer shapeRenderer,
        Runnable onSessionEnd
    ) {
        this(
            snakeSessionFactory,
            worldDimensions,
            shapeRenderer,
            new FitViewport(worldDimensions.width(), worldDimensions.height()),
            onSessionEnd
        );
    }

    @Override
    public void render(float delta) {
        for (var game : snakeSession.dominion().findCompositionsWith(Session.class)) {
            if (game.status == Session.Status.ENDED) {
                onSessionEnd.run();
                return;
            }
        }
        snakeSession.scheduler().tick((long) (TimeUnit.SECONDS.toNanos(1) * delta));
        snakeSession.standaloneRenderingSystem().render();
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
    }

    @Override
    public void show() {
        snakeSession = snakeSessionFactory.snakeSession(
            fitViewport,
            shapeRenderer,
            worldDimensions
        );
    }

    @Override
    public void hide() {
        snakeSession.scheduler().shutDown();
    }
}
