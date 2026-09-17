package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.util.Random;

final class BuildingPanel extends Table {

    BuildingPanel(
        AssetManager assetManager,
        Assets assets,
        EventListener eventListener,
        BigDecimalFormatter bigDecimalFormatter,
        Transaction transaction,
        BakeryService bakeryService,
        Random random
    ) {
        var buildings = Building.values();
        for (var i = 0; i < buildings.length; i++) {
            addAction(
                assetManager,
                assets,
                eventListener,
                bigDecimalFormatter,
                transaction,
                bakeryService,
                random,
                buildings,
                i
            );
        }
    }

    private void addAction(
        AssetManager assetManager,
        Assets assets,
        EventListener eventListener,
        BigDecimalFormatter bigDecimalFormatter,
        Transaction transaction,
        BakeryService bakeryService,
        Random random,
        Building[] buildings,
        int index
    ) {
        addAction(
            action(
                Actions.run(
                    () -> add(
                        buildingButton(
                            assetManager,
                            assets,
                            eventListener,
                            bigDecimalFormatter,
                            transaction,
                            bakeryService,
                            random,
                            buildings,
                            index
                        )
                    )
                        .row()
                ),
                bakeryService,
                buildings,
                index
            )
        );
    }

    private Action action(
        Action addButton,
        BakeryService bakeryService,
        Building[] buildings,
        int index
    ) {
        var previous = index - 2;
        if (previous < 0) {
            return addButton;
        }
        return new UnlockBuildingAction(
            bakeryService,
            buildings[previous],
            addButton
        );
    }

    private BuildingButton buildingButton(
        AssetManager assetManager,
        Assets assets,
        EventListener eventListener,
        BigDecimalFormatter bigDecimalFormatter,
        Transaction transaction,
        BakeryService bakeryService,
        Random random,
        Building[] buildings,
        int index
    ) {
        var buildingButton = new BuildingButton(
            this,
            assetManager,
            assets,
            bigDecimalFormatter,
            transaction,
            bakeryService,
            random,
            buildings[index],
            index
        );
        buildingButton.addListener(eventListener);
        return buildingButton;
    }
}
