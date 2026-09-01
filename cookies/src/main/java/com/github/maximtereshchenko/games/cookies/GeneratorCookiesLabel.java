package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class GeneratorCookiesLabel extends Label implements Subscriber<Event> {

    private final Skin skin;
    private final Generator generator;

    GeneratorCookiesLabel(
        Skin skin,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        super("15", skin, "label_generatorCookies_disabled");
        this.skin = skin;
        this.generator = generator;
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorUnlocked generatorUnlocked &&
            generatorUnlocked.value().equals(generator)
        ) {
            setStyle(skin.get("label_generatorCookies", LabelStyle.class));
        }
    }
}
