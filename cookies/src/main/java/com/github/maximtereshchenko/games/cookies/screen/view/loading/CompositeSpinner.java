package com.github.maximtereshchenko.games.cookies.screen.view.loading;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

final class CompositeSpinner extends Stack {

    CompositeSpinner(Skin skin) {
        add(
            new Spinner(
                skin,
                "bottom",
                5
            )
        );
        add(
            new Spinner(
                skin,
                "top",
                9
            )
        );
    }
}
