package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingTooltipHeader extends Table {

    BuildingTooltipHeader(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Building building
    ) {
        add(
            new BuildingNameLabel(
                skin,
                "label_buildingName_tooltip",
                bundle,
                bakeryService,
                building
            )
        )
            .left();
        add().growX();
        add(new Image(skin.get(Style.class).icon))
            .width(Value.prefWidth);
        add(
            new TransactionValueLabel(
                skin,
                bakeryService,
                building
            )
        )
            .row();
        add(
            new BuildingCountLabel(
                skin,
                "label_buildingCount_tooltip",
                bundle,
                "buildings.counts.tooltip.zero",
                "buildings.counts.tooltip",
                bakeryService,
                building
            )
        )
            .left();
    }

    private static final class Style {

        Drawable icon;
    }
}
