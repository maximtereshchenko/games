package com.github.maximtereshchenko.games.cookies.screen.view.game.options;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

public final class OptionsPanel extends Table {

    public OptionsPanel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        var gameAssets = assets.game();
        top();
        defaults()
            .pad(4)
            .growX()
            .uniformX();
        add(
            new SaveButton(
                assetManager,
                assets,
                bakeryService
            )
        );
        add(
            new Label(
                assetManager.get(gameAssets.bundle())
                    .get("options.button.save.description"),
                assetManager.get(gameAssets.skin()),
                "options-description"
            )
        )
            .colspan(5)
            .row();
        add(
            new VolumeWidget(
                assetManager,
                assets,
                bakeryService
            )
        )
            .colspan(2)
            .growX();
    }
}
