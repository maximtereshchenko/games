package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Event;

import java.util.Random;

public final class BakeryPanel extends Container<Stack> {

    public BakeryPanel(
        Skin skin,
        I18NBundle bundle,
        Random random,
        BakeryService bakeryService,
        EventBus<Event> eventBus
    ) {
        fill();
        clip();
        var stack = new Stack();
        stack.add(new BottomOverlayWidget(skin));
        stack.add(
            new FallingCookiesWidget(
                skin,
                random,
                eventBus
            )
        );
        stack.add(
            new CookieWidget(
                skin,
                random,
                bakeryService,
                eventBus
            )
        );
        stack.add(
            new BakingStatisticsWidget(
                skin,
                bundle,
                eventBus
            )
        );
        stack.add(new MilkWidget(skin));
        setActor(stack);
    }
}
