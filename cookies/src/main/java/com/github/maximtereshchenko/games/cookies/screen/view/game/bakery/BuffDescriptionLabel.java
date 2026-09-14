package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.domain.BuffDescription;
import com.github.maximtereshchenko.games.cookies.domain.FrenzyDescription;

final class BuffDescriptionLabel extends Label {

    BuffDescriptionLabel(
        Skin skin,
        String styleName,
        I18NBundle bundle,
        Buff buff,
        BuffDescription buffDescription
    ) {
        super(
            bundle.format(
                "buff.%s.description".formatted(buff.name()),
                switch (buffDescription) {
                    case FrenzyDescription frenzyDescription -> new Object[]{
                        frenzyDescription.multiplier(),
                        frenzyDescription.durationSeconds()
                    };
                }
            ),
            skin,
            styleName
        );
    }
}
