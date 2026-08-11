package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

final class BricksGame extends Game {

    private final ShapeRenderer shapeRenderer;

    BricksGame(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    public void create() {
        //empty
    }

    @Override
    public void dispose() {
        disposeScreen();
        shapeRenderer.dispose();
    }

    @Override
    public void setScreen(Screen screen) {
        disposeScreen();
        super.setScreen(screen);
    }

    private void disposeScreen() {
        var current = getScreen();
        if (current != null) {
            current.dispose();
        }
    }
}
