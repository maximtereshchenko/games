package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

final class FunctionalHoverListener extends InputListener {

    private final Runnable runnable;

    FunctionalHoverListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        runnable.run();
    }
}
