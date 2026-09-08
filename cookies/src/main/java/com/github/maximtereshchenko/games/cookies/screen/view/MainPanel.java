package com.github.maximtereshchenko.games.cookies.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.display.BuildingDisplayPanel;

import java.util.Random;

final class MainPanel extends Table {

    MainPanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Random random
    ) {
        defaults().growX();
        add(
            new NavigationPanel(
                skin,
                bundle
            )
        )
            .row();
        add(new HorizontalBeamWidget(skin))
            .row();
        add(
            new FocusableScrollPane<>(
                skin,
                new BuildingDisplayPanel(
                    skin,
                    bakeryService,
                    random
                )
            )
        )
            .growY();
    }
}
