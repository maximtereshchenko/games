package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.dominion.ecs.api.Dominion;

final class Main {

    static void main() {
        new Lwjgl3Application(
            new SnakeGame(new ScreenFactory(Dominion::create), ShapeRenderer::new),
            new Lwjgl3ApplicationConfiguration()
        );
    }
}
