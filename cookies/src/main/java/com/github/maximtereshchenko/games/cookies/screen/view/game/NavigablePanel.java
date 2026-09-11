package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

import java.util.Random;

final class NavigablePanel extends Table {

    NavigablePanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random
    ) {
        var mainPanel = new MainPanel(
            skin,
            bundle,
            bigDecimalFormatter,
            bakeryService,
            random
        );
        defaults().growX();
        add(
            new NavigationPanel(
                skin,
                bundle,
                new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        mainPanel.toggle();
                    }
                }
            )
        )
            .row();
        add(new HorizontalBeamWidget(skin))
            .row();
        add(
            new FocusableScrollPane<>(
                skin,
                mainPanel
            )
        )
            .growY();
    }
}
