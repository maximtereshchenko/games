package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;

import java.util.Set;

final class CookiesGame extends Game {

    private final Set<Disposable> disposables;

    CookiesGame(Set<Disposable> disposables) {
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

    private void disposeScreen() {
        var current = getScreen();
        if (current != null) {
            current.dispose();
        }
    }
}
