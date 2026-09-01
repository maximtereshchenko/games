package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class GeneratorIcon extends Image implements Subscriber<Event> {

    private final Generator generator;

    GeneratorIcon(
        Skin skin,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        super(
            skin,
            switch (generator) {
                case CURSOR -> "icon_cursor_tilted";
            }
        );
        this.generator = generator;
        setColor(skin.getColor("color_black_transparent80"));
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorUnlocked generatorUnlocked &&
            generatorUnlocked.value().equals(generator)
        ) {
            addAction(Actions.color(Color.WHITE, 0.5f));
        }
    }
}
