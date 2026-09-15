package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;
import com.github.maximtereshchenko.games.cookies.screen.view.game.ColoredButton;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;
import com.github.maximtereshchenko.games.cookies.screen.view.game.SameTopHeightTooltipWidget;

final class BuffWidget extends ColoredButton {

    private final BakeryService bakeryService;
    private final Buff buff;

    BuffWidget(
        Skin skin,
        I18NBundle bundle,
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
        var tooltipWidget = new SameTopHeightTooltipWidget(
            this,
            new PopUpFrame(
                skin,
                new BuffTooltipPanel(
                    skin,
                    bundle,
                    bakeryService,
                    buff
                )
            )
        );
        tooltipWidget.getContainer().minWidth(250);
        addListener(tooltipWidget);
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
