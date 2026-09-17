package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class StatisticsLine extends Table {

    StatisticsLine(
        AssetManager assetManager,
        Assets assets,
        String text,
        Actor... actors
    ) {
        defaults().padRight(4);
        add(
            new Label(
                text,
                assetManager.get(assets.game().skin()),
                "statistics-key"
            )
        );
        for (var actor : actors) {
            add(actor);
        }
    }
}
