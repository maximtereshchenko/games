package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class BuildingPanel extends Table {

    BuildingPanel(
        Skin skin,
        I18NBundle bundle,
        EventListener eventListener,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        var buildings = Building.values();
        for (var i = 0; i < buildings.length; i++) {
            addAction(
                skin,
                bundle,
                eventListener,
                bigDecimalFormatter,
                bakeryService,
                buildings,
                i
            );
        }
    }

    private void addAction(
        Skin skin,
        I18NBundle bundle,
        EventListener eventListener,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Building[] buildings,
        int index
    ) {
        addAction(
            action(
                Actions.run(
                    () -> add(
                        buildingButton(
                            skin,
                            bundle,
                            eventListener,
                            bigDecimalFormatter,
                            bakeryService,
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
        Skin skin,
        I18NBundle bundle,
        EventListener eventListener,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Building[] buildings,
        int index
    ) {
        var buildingButton = new BuildingButton(
            skin,
            bundle,
            bigDecimalFormatter,
            bakeryService,
            buildings[index],
            index
        );
        buildingButton.addListener(eventListener);
        return buildingButton;
    }
}
