package com.github.maximtereshchenko.snakes.screen.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class BasicButton extends TextButton {

    public BasicButton(String text, Skin skin) {
        super(text, skin);
    }

    public final void onHover(Runnable runnable) {
        addListener(
            new InputListener() {

                @Override
                public void enter(
                    InputEvent event,
                    float x,
                    float y,
                    int pointer,
                    Actor fromActor
                ) {
                    runnable.run();
                }
            }
        );
    }

    public final void onClick(Runnable runnable) {
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    runnable.run();
                }
            }
        );
    }
}
