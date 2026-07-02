package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.function.BiFunction;
import java.util.function.Supplier;

final class SnakeGame extends Game {

    private final Supplier<ShapeRenderer> shapeRendererSupplier;
    private final BiFunction<ShapeRenderer, Runnable, SnakeSessionScreen> snakeSessionScreenFunction;
    private ShapeRenderer shapeRenderer;
    private SnakeSessionScreen snakeSessionScreen;

    SnakeGame(
        Supplier<ShapeRenderer> shapeRendererSupplier,
        BiFunction<ShapeRenderer, Runnable, SnakeSessionScreen> snakeSessionScreenFunction
    ) {
        this.shapeRendererSupplier = shapeRendererSupplier;
        this.snakeSessionScreenFunction = snakeSessionScreenFunction;
    }

    SnakeGame(
        SnakeSessionFactory snakeSessionFactory,
        WorldDimensions worldDimensions
    ) {
        this(
            ShapeRenderer::new,
            (providedShapeRenderer, onSessionEnd) -> new SnakeSessionScreen(
                snakeSessionFactory,
                worldDimensions,
                providedShapeRenderer,
                onSessionEnd
            )
        );
    }

    @Override
    public void create() {
        shapeRenderer = shapeRendererSupplier.get();
        snakeSessionScreen = snakeSessionScreenFunction.apply(
            shapeRenderer,
            () -> setScreen(snakeSessionScreen)
        );
        setScreen(snakeSessionScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }
}
