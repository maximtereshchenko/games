package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.BuildingUnlocked;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class BuildingDescription extends Label implements Subscriber<Event> {

    private final I18NBundle bundle;
    private final Building building;

    BuildingDescription(
        Skin skin,
        I18NBundle bundle,
        Building building,
        EventBus<Event> eventBus
    ) {
        super(
            bundle.get(
                "buildings.descriptions.locked"
            ),
            skin,
            "label_buildingDescription"
        );
        this.bundle = bundle;
        this.building = building;
        setAlignment(Align.right);
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof BuildingUnlocked buildingUnlocked &&
            buildingUnlocked.building().equals(building)
        ) {
            setText(bundle.get("buildings.descriptions." + building.name()));
        }
    }
}
