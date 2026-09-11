package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.game.Badge;

final class BuildingCountBadge extends Badge {

    private final I18NBundle bundle;
    private final BakeryService bakeryService;
    private final Building building;

    BuildingCountBadge(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Building building
    ) {
        super(skin, "");
        this.bundle = bundle;
        this.bakeryService = bakeryService;
        this.building = building;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(
            bundle.format(
                "store.building.count.badge",
                bakeryService.count(building)
            )
        );
    }
}
