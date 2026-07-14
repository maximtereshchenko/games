package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

final class SnakeSessionScreen extends ScreenAdapter {

    private final WorldDimensions worldDimensions;
    private final ShapeRenderer shapeRenderer;
    private final Viewport viewport;
    private final ApplicationEvents applicationEvents;
    private final SnakeSessionFactory snakeSessionFactory;
    private SnakeSession snakeSession;

    SnakeSessionScreen(
        WorldDimensions worldDimensions,
        ShapeRenderer shapeRenderer,
        Viewport viewport,
        ApplicationEvents applicationEvents,
        SnakeSessionFactory snakeSessionFactory
    ) {
        this.worldDimensions = worldDimensions;
        this.shapeRenderer = shapeRenderer;
        this.viewport = viewport;
        this.applicationEvents = applicationEvents;
        this.snakeSessionFactory = snakeSessionFactory;
    }

    @Override
    public void render(float delta) {
        if (value(Session.class, session -> session.status == Session.Status.ENDED, false)) {
            applicationEvents.publish(
                new SnakeSessionEnded(
                    value(
                        LeftTurns.class,
                        leftTurns -> leftTurns.value,
                        0
                    )
                )
            );
            return;
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

    private <T, R> R value(Class<T> type, Function<T, R> function, R defaultValue) {
        for (var component : snakeSession.dominion().findCompositionsWith(type)) {
            return function.apply(component);
        }
        return defaultValue;
    }
}
