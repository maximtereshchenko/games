package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.FlavorTextLabel;

final class BuildingFlavorTextLabel extends FlavorTextLabel {

    BuildingFlavorTextLabel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Building building
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        super(
            assetManager,
            assets,
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
