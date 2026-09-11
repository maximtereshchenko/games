package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BeamWidget;

final class StoreBeamWidget extends BeamWidget {

    private final Label label;

    StoreBeamWidget(Skin skin, String text) {
        super(skin, "store");
        this.label = new Label(text, skin, "beam-store");
        label.setVisible(false);
        add(label);
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
                setVisible(
                    pointer,
                    fromActor,
                    event.getListenerActor(),
                    true
                );
            }

            @Override
            public void exit(
                InputEvent event,
                float x,
                float y,
                int pointer,
                Actor toActor
            ) {
                setVisible(
                    pointer,
                    toActor,
                    event.getListenerActor(),
                    false
                );
            }
        };
    }

    private void setVisible(
        int pointer,
        Actor related,
        Actor listenerActor,
        boolean isVisible
    ) {
        if (pointer == -1 && !isInside(related, listenerActor)) {
            label.setVisible(isVisible);
        }
    }

    private boolean isInside(Actor related, Actor listenerActor) {
        return related != null && related.isDescendantOf(listenerActor);
    }
}
