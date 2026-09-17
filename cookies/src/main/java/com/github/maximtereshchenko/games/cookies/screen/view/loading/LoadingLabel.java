package com.github.maximtereshchenko.games.cookies.screen.view.loading;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class LoadingLabel extends Label {

    LoadingLabel(AssetManager assetManager, Assets assets) {
        super(
            assetManager.get(assets.loading().bundle()).get("loading.text"),
            assetManager.get(assets.loading().skin())
        );
        var duration = 0.5f;
        addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.alpha(0.5f, duration),
                    Actions.fadeIn(duration)
                )
            )
        );
    }
}
