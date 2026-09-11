package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.util.Random;

public final class BakeryPanel extends Container<Stack> {

    public BakeryPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random
    ) {
        fill();
        clip();
        var cookieWidget = new CookieWidget(
            skin,
            bigDecimalFormatter,
            bakeryService,
            random
        );
        var fallingCookiesWidget = new FallingCookiesWidget(
            skin,
            bakeryService,
            random
        );
        cookieWidget.addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    fallingCookiesWidget.addFallingCookieWidget();
                }
            }
        );
        var stack = new Stack();
        stack.add(new BottomOverlayWidget(skin));
        stack.add(fallingCookiesWidget);
        stack.add(cookieWidget);
        stack.add(
            new BakingStatisticsWidget(
                skin,
                bundle,
                bigDecimalFormatter,
                bakeryService
            )
        );
        stack.add(new MilkWidget(skin));
        setActor(stack);
    }
}
