package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Buff;

final class BuffNameLabel extends Label {

    BuffNameLabel(
        Skin skin,
        String styleName,
        I18NBundle bundle,
        Buff buff
    ) {
        super(
            bundle.get("buff.%s.name".formatted(buff.name())),
            skin,
            styleName
        );
    }
}
