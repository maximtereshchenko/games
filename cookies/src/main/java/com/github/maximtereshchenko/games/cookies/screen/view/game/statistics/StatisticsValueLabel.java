package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

abstract class StatisticsValueLabel extends Label {

    private final BakeryService bakeryService;

    StatisticsValueLabel(AssetManager assetManager, Assets assets, BakeryService bakeryService) {
        super("", assetManager.get(assets.game().skin()), "statistics-value");
        this.bakeryService = bakeryService;
    }

    @Override
    public final void act(float delta) {
        super.act(delta);
        setText(text(bakeryService));
    }

    abstract String text(BakeryService bakeryService);
}
