package com.github.maximtereshchenko.games.snakes.screen.view.settings;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;
import com.github.maximtereshchenko.games.snakes.screen.view.BasicButton;

import java.util.function.Consumer;

public final class SettingsView extends ScreenLayout {

    private final MusicVolumeSlider musicVolumeSlider;
    private final BasicButton backButton;

    public SettingsView(I18NBundle bundle, Skin skin, float volume) {
        musicVolumeSlider = new MusicVolumeSlider(skin, volume);
        backButton = new BasicButton(bundle.get("screens.settings.buttons.back"), skin);
        defaults().growX().pad(3);
        add(new Label(bundle.get("screens.settings.sliders.music.volume"), skin));
        add(musicVolumeSlider).row();
        add(backButton).colspan(2);
    }

    public void onFinish(Runnable runnable) {
        backButton.onClick(runnable);
    }

    public void onVolumeChange(Consumer<Float> consumer) {
        musicVolumeSlider.onVolumeChange(consumer);
    }
}
