package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class UpgradeFlavorTextLabel extends FlavorTextLabel {

    UpgradeFlavorTextLabel(
        AssetManager assetManager,
        Assets assets,
        Upgrade upgrade
    ) {
        super(
            assetManager,
            assets,
            assetManager.get(assets.game().bundle()).get(
                "store.upgrade.%s.flavor-text".formatted(upgrade.name())
            )
        );
    }
}
