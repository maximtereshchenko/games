package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.event.Subscriber;

final class BricksGame extends Game implements Subscriber<Event> {

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

    @Override
    public void onEvent(Event event) {
        Gdx.app.exit();
    }

    private void disposeScreen() {
        var current = getScreen();
        if (current != null) {
            current.dispose();
        }
    }
}
