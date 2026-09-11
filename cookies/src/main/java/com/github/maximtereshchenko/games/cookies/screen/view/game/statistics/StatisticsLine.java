package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

final class StatisticsLine extends Table {

    StatisticsLine(
        Skin skin,
        String text,
        Actor... actors
    ) {
        defaults().padRight(4);
        add(
            new Label(
                text,
                skin,
                "statistics-key"
            )
        );
        for (var actor : actors) {
            add(actor);
        }
    }
}
