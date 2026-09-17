package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.Badge;

final class BuildingCountBadge extends Badge {

    private final I18NBundle bundle;
    private final BakeryService bakeryService;
    private final Building building;

    BuildingCountBadge(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Building building
    ) {
        super(assetManager, assets, "");
        this.bundle = assetManager.get(assets.game().bundle());
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
