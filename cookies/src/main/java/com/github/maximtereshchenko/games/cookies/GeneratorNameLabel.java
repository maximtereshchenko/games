package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class GeneratorNameLabel extends Label implements Subscriber<Event> {

    private final I18NBundle bundle;
    private final Generator generator;

    GeneratorNameLabel(
        Skin skin,
        I18NBundle bundle,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        super(
            bundle.get("generators.locked.name"),
            skin,
            "label_generatorName_" + generator
        );
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
            setText(bundle.get("generators.%s.name".formatted(generator)));
        }
    }
}
