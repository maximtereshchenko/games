package com.github.maximtereshchenko.games.cookies.screen.view.display;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.BeamWidget;

import java.util.Random;

public final class BuildingDisplayPanel extends Table {

    public BuildingDisplayPanel(
        Skin skin,
        BakeryService bakeryService,
        Random random
    ) {
        top();
        defaults().growX();
        for (var building : Building.values()) {
            if (building != Building.CURSOR) {
                add(
                    new BuildingDisplayWidget(
                        skin,
                        bakeryService,
                        building,
                        random
                    )
                )
                    .row();
                add(new BeamWidget(skin, "horizontal")).row();
            }
        }
    }
}
