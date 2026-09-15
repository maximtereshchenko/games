package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.domain.FrenzyEffect;

final class BuffDescriptionLabel extends Label {

    private final I18NBundle bundle;
    private final BakeryService bakeryService;
    private final Buff buff;

    BuffDescriptionLabel(
        Skin skin,
        String styleName,
        I18NBundle bundle,
        BakeryService bakeryService,
        Buff buff
    ) {
        super("", skin, styleName);
        this.bundle = bundle;
        this.bakeryService = bakeryService;
        this.buff = buff;
        setAlignment(Align.center);
        setWrap(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(
            bundle.format(
                "buff.%s.description".formatted(buff.name()),
                switch (bakeryService.buffEffect(buff)) {
                    case FrenzyEffect frenzyDescription -> new Object[]{
                        frenzyDescription.multiplier(),
                        bakeryService.buffInterval(buff).durationSeconds()
                    };
                }
            )
        );
    }
}
