package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class BuildingTooltip extends Table {

    private final BuildingTooltipHeader buildingTooltipHeader;

    BuildingTooltip(
        Skin skin,
        I18NBundle bundle,
        Building building,
        EventBus<Event> eventBus
    ) {
        this.buildingTooltipHeader = new BuildingTooltipHeader(
            skin,
            bundle,
            building,
            eventBus
        );
        defaults().padBottom(8);
        add(
            new BuildingIcon(
                skin,
                "icon_%s_buildingTooltip".formatted(building.name()),
                0,
                building,
                eventBus
            )
        )
            .width(Value.prefWidth);
        add(buildingTooltipHeader).growX().row();
        add(new Image(skin.get(Style.class).drawable))
            .colspan(2)
            .growX()
            .row();
        add(
            new BuildingDescription(
                skin,
                bundle,
                building,
                eventBus
            )
        )
            .colspan(2)
            .growX();
    }

    void setDisabled(boolean isDisabled) {
        buildingTooltipHeader.setDisabled(isDisabled);
    }

    private static final class Style {

        Drawable drawable;
    }
}
