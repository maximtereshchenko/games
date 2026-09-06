package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingFlavorTextLabel extends Label {

    BuildingFlavorTextLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Building building
    ) {
        super(
            bundle.get(
                "buildings.flavorTexts.locked"
            ),
            skin,
            "label_buildingFlavorText"
        );
        addAction(
            new UnlockBuildingAction(
                bakeryService,
                building,
                Actions.run(
                    () -> setText(
                        bundle.get("buildings.flavorTexts." + building.name())
                    )
                )
            )
        );
    }
}
