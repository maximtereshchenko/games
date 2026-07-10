package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

final class FunctionalClickListener extends ClickListener {

    private final Runnable runnable;

    FunctionalClickListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        runnable.run();
    }
}
