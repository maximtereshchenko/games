package com.github.maximtereshchenko.games.cookies.screen.view.loading;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class CompositeSpinner extends Stack {

    CompositeSpinner(AssetManager assetManager, Assets assets) {
        add(
            new Spinner(
                assetManager,
                assets,
                "bottom",
                5
            )
        );
        add(
            new Spinner(
                assetManager,
                assets,
                "top",
                9
            )
        );
    }
}
