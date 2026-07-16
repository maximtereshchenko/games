package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

final class FunctionalInputListener extends InputListener {

    private final int keyCode;
    private final Runnable runnable;

    FunctionalInputListener(int keyCode, Runnable runnable) {
        this.keyCode = keyCode;
        this.runnable = runnable;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keyCode == keycode) {
            runnable.run();
            return true;
        }
        return false;
    }
}
