package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

final class UpgradeFlavorTextLabel extends FlavorTextLabel {

    UpgradeFlavorTextLabel(
        Skin skin,
        I18NBundle bundle,
        Upgrade upgrade
    ) {
        super(
            skin,
            bundle.get(
                "store.upgrade.%s.flavor-text".formatted(upgrade.name())
            )
        );
    }
}
