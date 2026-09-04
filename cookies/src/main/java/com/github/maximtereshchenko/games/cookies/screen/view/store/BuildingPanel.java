package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class BuildingPanel extends Table {

    BuildingPanel(
        Skin skin,
        I18NBundle bundle,
        EventListener eventListener,
        BakeryService bakeryService,
        EventBus<Event> eventBus
    ) {
        for (var building : Building.values()) {
            var buildingButton = new BuildingButton(
                skin,
                bundle,
                building,
                bakeryService,
                eventBus
            );
            buildingButton.addListener(eventListener);
            add(buildingButton).row();
        }
    }
}
