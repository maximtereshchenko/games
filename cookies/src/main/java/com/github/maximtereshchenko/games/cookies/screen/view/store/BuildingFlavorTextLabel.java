package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.BuildingUnlocked;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class BuildingFlavorTextLabel extends Label implements Subscriber<Event> {

    private final I18NBundle bundle;
    private final Building building;

    BuildingFlavorTextLabel(
        Skin skin,
        I18NBundle bundle,
        Building building,
        EventBus<Event> eventBus
    ) {
        super(
            bundle.get(
                "buildings.flavorTexts.locked"
            ),
            skin,
            "label_buildingFlavorText"
        );
        this.bundle = bundle;
        this.building = building;
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof BuildingUnlocked buildingUnlocked &&
            buildingUnlocked.building().equals(building)
        ) {
            setText(bundle.get("buildings.flavorTexts." + building.name()));
        }
    }
}
