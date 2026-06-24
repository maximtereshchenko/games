package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

final class Main {

    static void main() {
        new Lwjgl3Application(
            new SnakeApplicationListener(),
            new Lwjgl3ApplicationConfiguration()
        );
    }
}
