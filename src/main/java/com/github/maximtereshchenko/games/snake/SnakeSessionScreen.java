package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.TimeUnit;

final class SnakeSessionScreen extends ScreenAdapter {

    private final SnakeSessionFactory snakeSessionFactory;
    private final WorldDimensions worldDimensions;
    private final ShapeRenderer shapeRenderer;
    private final Viewport viewport;
    private final ApplicationEvents applicationEvents;
    private SnakeSession snakeSession;

    SnakeSessionScreen(
        SnakeSessionFactory snakeSessionFactory,
        WorldDimensions worldDimensions,
        ShapeRenderer shapeRenderer,
        Viewport viewport,
        ApplicationEvents applicationEvents
    ) {
        this.snakeSessionFactory = snakeSessionFactory;
        this.worldDimensions = worldDimensions;
        this.shapeRenderer = shapeRenderer;
        this.viewport = viewport;
        this.applicationEvents = applicationEvents;
    }

    @Override
    public void render(float delta) {
        for (var game : snakeSession.dominion().findCompositionsWith(Session.class)) {
            if (game.status == Session.Status.ENDED) {
                applicationEvents.publish(ApplicationEvent.SNAKE_SESSION_ENDED);
                return;
            }
        }
        snakeSession.scheduler().tick((long) (TimeUnit.SECONDS.toNanos(1) * delta));
        snakeSession.standaloneRenderingSystem().render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        snakeSession = snakeSessionFactory.snakeSession(
            viewport,
            shapeRenderer,
            worldDimensions
        );
    }

    @Override
    public void hide() {
        snakeSession.scheduler().shutDown();
    }
}
