package com.github.maximtereshchenko.games.cookies.screen.view.game.display;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.game.HorizontalBeamWidget;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public final class BuildingDisplayPanel extends Table {

    private final Map<Building, BuildingDisplayWidget> buildingDisplayWidgets;

    public BuildingDisplayPanel(
        Skin skin,
        BakeryService bakeryService,
        Random random
    ) {
        this.buildingDisplayWidgets = new EnumMap<>(Building.class);
        top();
        defaults().growX();
        for (var building : Building.values()) {
            if (building != Building.CURSOR) {
                addAction(
                    Actions.sequence(
                        new BuildingCountAction(
                            bakeryService,
                            building,
                            Actions.run(
                                () -> addBuildingDisplayWidget(
                                    skin,
                                    bakeryService,
                                    building,
                                    random
                                )
                            )
                        )
                    )
                );
            }
        }
    }

    private void addBuildingDisplayWidget(
        Skin skin,
        BakeryService bakeryService,
        Building building,
        Random random
    ) {
        buildingDisplayWidgets.put(
            building,
            new BuildingDisplayWidget(
                skin,
                bakeryService,
                building,
                random
            )
        );
        clearChildren();
        for (var buildingDisplayWidget : buildingDisplayWidgets.values()) {
            add(buildingDisplayWidget).row();
            add(new HorizontalBeamWidget(skin)).row();
        }
    }
}
