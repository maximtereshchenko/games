package com.github.maximtereshchenko.games.cookies.screen.view.loading;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

final class LoadingLabel extends Label {

    LoadingLabel(Skin skin, I18NBundle bundle) {
        super(bundle.get("loading.text"), skin);
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
