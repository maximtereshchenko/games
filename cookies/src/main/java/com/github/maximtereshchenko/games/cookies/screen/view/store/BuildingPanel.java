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
        for (var generator : Building.values()) {
            var generatorButton = new BuildingButton(
                skin,
                bundle,
                generator,
                bakeryService,
                eventBus
            );
            generatorButton.addListener(eventListener);
            add(generatorButton).row();
        }
    }
}
