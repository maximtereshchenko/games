package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class BuildingCountLabel extends Label {

    private final BakeryService bakeryService;
    private final Building building;

    BuildingCountLabel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Building building
    ) {
        super("", assetManager.get(assets.game().skin()), "building-count");
        this.bakeryService = bakeryService;
        this.building = building;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(text(bakeryService.count(building)));
    }

    private String text(int count) {
        if (count == 0) {
            return "";
        }
        return String.valueOf(count);
    }
}
