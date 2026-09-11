package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

abstract class StatisticsValueLabel extends Label {

    private final BakeryService bakeryService;

    StatisticsValueLabel(Skin skin, BakeryService bakeryService) {
        super("", skin, "statistics-value");
        this.bakeryService = bakeryService;
    }

    @Override
    public final void act(float delta) {
        super.act(delta);
        setText(text(bakeryService));
    }

    abstract String text(BakeryService bakeryService);
}
