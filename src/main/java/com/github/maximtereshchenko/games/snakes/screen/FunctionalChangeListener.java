package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

final class FunctionalChangeListener extends ChangeListener {

    private final Runnable runnable;

    FunctionalChangeListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        runnable.run();
    }
}
