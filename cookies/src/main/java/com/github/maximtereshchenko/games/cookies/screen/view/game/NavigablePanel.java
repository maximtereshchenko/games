package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.time.Clock;
import java.util.Random;

final class NavigablePanel extends Table {

    NavigablePanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random,
        Clock clock
    ) {
        var mainPanel = new MainPanel(
            assetManager,
            assets,
            bigDecimalFormatter,
            bakeryService,
            random,
            clock
        );
        defaults().growX();
        add(
            new NavigationPanel(
                assetManager,
                assets,
                new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        mainPanel.toggle();
                    }
                }
            )
        )
            .row();
        add(new HorizontalBeamWidget(assetManager, assets))
            .row();
        add(
            new FocusableScrollPane<>(
                assetManager,
                assets,
                mainPanel
            )
        )
            .growY();
    }
}
