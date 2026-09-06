package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class BuildingCountLabel extends Label {

    private final I18NBundle bundle;
    private final String zeroValueKey;
    private final String valueKey;
    private final BakeryService bakeryService;
    private final Building building;

    BuildingCountLabel(
        Skin skin,
        String styleName,
        I18NBundle bundle,
        String zeroValueKey,
        String valueKey,
        BakeryService bakeryService,
        Building building
    ) {
        super("", skin, styleName);
        this.bundle = bundle;
        this.zeroValueKey = zeroValueKey;
        this.valueKey = valueKey;
        this.bakeryService = bakeryService;
        this.building = building;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        var count = bakeryService.count(building);
        setText(bundle.format(key(count), count));
    }

    private String key(int count) {
        if (count == 0) {
            return zeroValueKey;
        }
        return valueKey;
    }
}
