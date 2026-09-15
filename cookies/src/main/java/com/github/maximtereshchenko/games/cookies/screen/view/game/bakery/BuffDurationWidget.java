package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;

final class BuffDurationWidget extends Image {

    private final Skin skin;
    private final Style style;
    private final BakeryService bakeryService;
    private final Buff buff;

    BuffDurationWidget(
        Skin skin,
        BakeryService bakeryService,
        Buff buff
    ) {
        this.skin = skin;
        this.style = skin.get(Style.class);
        this.bakeryService = bakeryService;
        this.buff = buff;
        setColor(style.color);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setDrawable(
            skin.getDrawable(
                "%s_%d".formatted(
                    style.baseIconName,
                    (int) Math.floor(
                        bakeryService.buffInterval(buff).progress() * style.max
                    )
                )
            )
        );
    }

    private static final class Style {

        String baseIconName;
        int max;
        Color color;
    }
}
