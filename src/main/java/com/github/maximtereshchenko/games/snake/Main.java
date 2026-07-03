package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;

final class Main {

    static void main() {
        var worldDimensions = new WorldDimensions(6, 6);
        new Lwjgl3Application(
            new SnakesGame(
                ShapeRenderer::new,
                (providedShapeRenderer, onSessionEnd) -> new SnakeSessionScreen(
                    new SnakeSessionFactory(Dominion::create),
                    worldDimensions,
                    providedShapeRenderer,
                    new FitViewport(worldDimensions.width(), worldDimensions.height()),
                    onSessionEnd
                )
            )
        );
    }
}
