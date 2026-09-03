package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.BakingRateUpdated;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class BakingRateLabel extends Label implements Subscriber<Event> {

    private final I18NBundle bundle;

    BakingRateLabel(
        Skin skin,
        I18NBundle bundle,
        EventBus<Event> eventBus
    ) {
        super(
            "",
            skin,
            "label_bakingRate"
        );
        this.bundle = bundle;
        setAlignment(Align.center);
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof BakingRateUpdated bakingRateUpdated) {
            setText(
                bundle.format(
                    "views.bakery.panels.bakery.labels.bakingRate",
                    bakingRateUpdated.value()
                )
            );
        }
    }
}
