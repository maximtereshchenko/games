package com.github.maximtereshchenko.games.cookies.screen.view.game.options;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class SaveButton extends TextButton {

    SaveButton(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        var gameAssets = assets.game();
        super(
            assetManager.get(
                    gameAssets.bundle()
                )
                .get("options.button.save"),
            assetManager.get(gameAssets.skin()),
            "save"
        );
        getLabel().setAlignment(Align.right);
        pad(4).padRight(8);
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    bakeryService.flush();
                }
            }
        );
    }
}
