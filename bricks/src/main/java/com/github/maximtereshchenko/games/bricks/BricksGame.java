package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.event.Subscriber;

import java.util.Set;

final class BricksGame extends Game implements Subscriber<Event> {

    private final Set<Disposable> disposables;

    BricksGame(Set<Disposable> disposables) {
        this.disposables = disposables;
    }

    @Override
    public void create() {
        //empty
    }

    @Override
    public void dispose() {
        disposeScreen();
        disposables.forEach(Disposable::dispose);
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
