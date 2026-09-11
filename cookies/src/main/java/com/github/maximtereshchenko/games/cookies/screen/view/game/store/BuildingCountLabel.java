package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingCountLabel extends Label {

    private final BakeryService bakeryService;
    private final Building building;

    BuildingCountLabel(
        Skin skin,
        BakeryService bakeryService,
        Building building
    ) {
        super("", skin, "building-count");
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
