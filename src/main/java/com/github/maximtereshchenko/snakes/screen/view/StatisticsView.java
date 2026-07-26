package com.github.maximtereshchenko.snakes.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.snakes.UserProfile;
import com.github.maximtereshchenko.snakes.UserProfileStatistics;

public final class StatisticsView extends Table {

    private final BasicButton backButton;

    public StatisticsView(I18NBundle bundle, Skin skin, UserProfile userProfile) {
        backButton = new BasicButton(bundle.get("screens.statistics.buttons.back"), skin);
        defaults().growX().pad(3);
        for (var userProfileStatistics : UserProfileStatistics.values()) {
            add(new Label(bundle.get("screens.statistics." + userProfileStatistics), skin));
            add(new Label(String.valueOf(userProfile.value(userProfileStatistics)), skin)).row();
        }
        add(backButton).colspan(2);
    }

    public void onFinish(Runnable runnable) {
        backButton.onClick(runnable);
    }
}
