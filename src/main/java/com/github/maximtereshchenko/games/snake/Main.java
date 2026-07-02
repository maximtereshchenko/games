package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import dev.dominion.ecs.api.Dominion;

final class Main {

    static void main() {
        new Lwjgl3Application(
            new SnakeGame(
                new SnakeSessionFactory(Dominion::create),
                new WorldDimensions(6, 6)
            )
        );
    }
}
