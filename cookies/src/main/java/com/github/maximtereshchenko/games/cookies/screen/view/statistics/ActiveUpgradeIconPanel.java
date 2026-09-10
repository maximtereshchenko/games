package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.view.UpgradeIcon;

final class ActiveUpgradeIconPanel extends HorizontalGroup {

    private final Skin skin;
    private final BakeryService bakeryService;

    ActiveUpgradeIconPanel(Skin skin, BakeryService bakeryService) {
        this.skin = skin;
        this.bakeryService = bakeryService;
        wrap();
        rowAlign(Align.left);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        clearChildren();
        for (var upgrade : Upgrade.values()) {
            if (bakeryService.isActive(upgrade)) {
                addActor(
                    new UpgradeIcon(
                        skin,
                        upgrade
                    )
                );
            }
        }
    }
}
