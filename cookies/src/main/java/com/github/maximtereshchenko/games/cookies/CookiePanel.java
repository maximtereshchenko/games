package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;

import java.util.Random;

final class CookiePanel extends Container<Stack> {

    CookiePanel(
        Skin skin,
        I18NBundle bundle,
        Random random,
        CookieService cookieService,
        EventBus<Event> eventBus
    ) {
        fill();
        clip();
        var stack = new Stack();
        stack.add(new GradientPanelLayer(skin));
        stack.add(new StaticFallingCookiesWidget(skin));
        stack.add(
            new DynamicFallingCookiesWidget(
                skin,
                random,
                eventBus
            )
        );
        stack.add(
            new CookieButtonPanelLayer(
                skin,
                random,
                cookieService,
                eventBus
            )
        );
        stack.add(
            new CookiesPanelLayer(
                skin,
                bundle,
                eventBus
            )
        );
        stack.add(new FlowingMilkLayer(skin));
        setActor(stack);
    }
}
