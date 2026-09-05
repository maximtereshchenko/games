package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class BuildingTooltip extends Table {

    BuildingTooltip(
        Skin skin,
        I18NBundle bundle,
        Building building,
        EventBus<Event> eventBus
    ) {
        add(
            new BuildingIcon(
                skin,
                "icon_%s_buildingTooltip".formatted(building.name()),
                0,
                building,
                eventBus
            )
        );
    }

    void setDisabled(boolean isDisabled) {

    }
}
