package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.github.maximtereshchenko.games.common.event.EventBus;

import java.util.Random;

final class CookiePanel extends Container<Stack> {

    CookiePanel(
        Skin skin,
        Random random,
        CookieService cookieService,
        EventBus<Event> eventBus
    ) {
        fill();
        clip();
        var stack = new Stack();
        stack.add(new GradientPanelLayer(skin));
        stack.add(new StaticFallingCookiesWidget(skin));
        stack.add(new DynamicFallingCookiesWidget(skin, random));
        stack.add(
            new CookieButtonPanelLayer(
                skin,
                random,
                cookieService
            )
        );
        stack.add(new CookiesPanelLayer(skin, eventBus));
        stack.add(new FlowingMilkLayer(skin));
        setActor(stack);
    }
}
