package com.github.maximtereshchenko.games.cookies.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.screen.view.bakery.BakeryPanel;
import com.github.maximtereshchenko.games.cookies.screen.view.store.StorePanel;

import java.util.Random;

public final class BakeryView extends ScreenLayout {

    public BakeryView(
        Skin skin,
        I18NBundle bundle,
        Random random,
        BakeryService bakeryService,
        EventBus<Event> eventBus
    ) {
        setBackground(skin.get(Style.class).background);
        defaults().growY();
        add(
            new BakeryPanel(
                skin,
                bundle,
                random,
                bakeryService,
                eventBus
            )
        )
            .width(Value.percentWidth(0.3f, this));
        addBeam(skin);
        add().growX();
        addBeam(skin);
        add(
            new StorePanel(
                skin,
                bundle,
                bakeryService,
                eventBus
            )
        );
    }

    private void addBeam(Skin skin) {
        add(new BeamWidget(skin, "widget_view")).width(Value.prefWidth);
    }

    private static final class Style {

        Drawable background;
    }
}
