package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

class AchievementIcon extends Image {

    private final Style style;
    private final BakeryService bakeryService;
    private final Achievement achievement;

    AchievementIcon(
        Skin skin,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        this.style = skin.get(achievement.name(), Style.class);
        this.bakeryService = bakeryService;
        this.achievement = achievement;
        setDrawable(drawable());
    }

    @Override
    public final void act(float delta) {
        super.act(delta);
        setDrawable(drawable());
    }

    private Drawable drawable() {
        if (bakeryService.isUnlocked(achievement)) {
            return style.unlocked;
        }
        return style.locked;
    }

    private static final class Style {

        Drawable unlocked;
        Drawable locked;
    }
}
