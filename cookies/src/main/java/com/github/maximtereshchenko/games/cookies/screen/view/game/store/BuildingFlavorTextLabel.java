package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.game.FlavorTextLabel;

final class BuildingFlavorTextLabel extends FlavorTextLabel {

    BuildingFlavorTextLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Building building
    ) {
        super(
            skin,
            bundle.get("store.building.locked.flavor-text")
        );
        addAction(
            new UnlockBuildingAction(
                bakeryService,
                building,
                Actions.run(
                    () -> setText(
                        bundle.get(
                            "store.building.%s.flavor-text"
                                .formatted(building.name())
                        )
                    )
                )
            )
        );
    }
}
