package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class GeneratorNameLabel extends Label implements Subscriber<Event> {

    private final Skin skin;
    private final I18NBundle bundle;
    private final Generator generator;

    GeneratorNameLabel(
        Skin skin,
        I18NBundle bundle,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        super("???", skin, "label_generatorName_disabled");
        this.skin = skin;
        this.bundle = bundle;
        this.generator = generator;
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorUnlocked generatorUnlocked &&
            generatorUnlocked.value().equals(generator)
        ) {
            setStyle(skin.get("label_generatorName", LabelStyle.class));
            setText(bundle.get("generators.%s.name".formatted(generator)));
        }
    }
}
