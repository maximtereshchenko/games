package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingNameLabel extends Label {

    BuildingNameLabel(
        Skin skin,
        String styleName,
        I18NBundle bundle,
        BakeryService bakeryService,
        Building building
    ) {
        super(
            bundle.get("store.building.locked.name"),
            skin,
            styleName
        );
        addAction(
            new UnlockBuildingAction(
                bakeryService,
                building,
                Actions.run(
                    () -> setText(
                        bundle.get("store.building.%s.name".formatted(building.name()))
                    )
                )
            )
        );
    }
}
