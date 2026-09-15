package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.screen.view.game.ColoredButton;

final class BuffWidget extends ColoredButton {

    private final BakeryService bakeryService;
    private final Buff buff;

    BuffWidget(
        Skin skin,
        BakeryService bakeryService,
        Buff buff
    ) {
        var stack = new Stack();
        stack.add(
            new BuffIcon(
                skin,
                buff
            )
        );
        stack.add(
            new BuffDurationWidget(
                skin,
                bakeryService,
                buff
            )
        );
        super(skin, stack);
        this.bakeryService = bakeryService;
        this.buff = buff;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setVisible(
            bakeryService.buffInterval(buff)
                .remainingSeconds() != 0
        );
    }
}
