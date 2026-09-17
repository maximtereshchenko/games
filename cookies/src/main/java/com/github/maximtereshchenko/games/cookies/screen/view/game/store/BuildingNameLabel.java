package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class BuildingNameLabel extends Label {

    BuildingNameLabel(
        AssetManager assetManager,
        Assets assets,
        String styleName,
        BakeryService bakeryService,
        Building building
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        super(
            bundle.get("store.building.locked.name"),
            assetManager.get(assets.game().skin()),
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
