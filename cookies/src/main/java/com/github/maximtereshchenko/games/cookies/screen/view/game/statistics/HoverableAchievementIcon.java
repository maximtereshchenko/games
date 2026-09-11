package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class HoverableAchievementIcon extends AchievementIcon {

    HoverableAchievementIcon(
        Skin skin,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        super(skin, bakeryService, achievement);
        var style = skin.get(Style.class);
        setColor(style.locked);
        var listener = new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                setColor(style.over);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                setColor(style.locked);
            }
        };
        addListener(listener);
        addAction(
            new UnlockAchievementAction(
                bakeryService,
                achievement,
                Actions.sequence(
                    Actions.removeListener(listener, false),
                    Actions.color(style.unlocked)
                )
            )
        );
    }

    private static final class Style {

        Color locked;
        Color unlocked;
        Color over;
    }
}
