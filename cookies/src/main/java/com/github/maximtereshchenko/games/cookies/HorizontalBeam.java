package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

final class HorizontalBeam extends Stack {

    private final Label label;

    HorizontalBeam(Skin skin, String text) {
        this.label = new Label(text, skin, "label_separator");
        this.label.setVisible(false);
        var container = new Container<>();
        container.background(skin.getTiledDrawable("tile_beam_horizontal"));
        add(container);
        add(new Image(skin, "gradient_left"));
        add(new Image(skin, "gradient_right"));
        add(new Container<>(label).left().padLeft(5));
        addListener(eventListener());
    }

    EventListener eventListener() {
        return new InputListener() {

            @Override
            public void enter(
                InputEvent event,
                float x,
                float y,
                int pointer,
                Actor fromActor
            ) {
                label.setVisible(true);
            }

            @Override
            public void exit(
                InputEvent event,
                float x,
                float y,
                int pointer,
                Actor toActor
            ) {
                if (pointer == -1) {
                    label.setVisible(false);
                }
            }
        };
    }
}
