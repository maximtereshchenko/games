package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.domain.GeneratorBought;

final class BuildingCountLabel extends Label implements Subscriber<Event> {

    private final Building building;

    BuildingCountLabel(
        Skin skin,
        Building building,
        EventBus<Event> eventBus
    ) {
        super("", skin, "label_generatorAmount");
        this.building = building;
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorBought generatorBought &&
            generatorBought.generator() == building
        ) {
            setText(generatorBought.newAmount());
        }
    }
}
