package com.github.maximtereshchenko.games.bricks.screen.view.settings;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.screen.view.Root;

import java.util.function.Consumer;

public final class SettingsView extends Root {

    private final VolumeSlider musicVolumeSlider;
    private final VolumeSlider soundVolumeSlider;
    private final Button backButton;

    public SettingsView(
        I18NBundle bundle,
        Skin skin,
        Configuration configuration,
        UserProfile userProfile,
        AssetManager assetManager
    ) {
        super(configuration, assetManager);
        this.musicVolumeSlider = new VolumeSlider(
            skin,
            userProfile.musicVolume()
        );
        this.soundVolumeSlider = new VolumeSlider(
            skin,
            userProfile.soundVolume()
        );
        this.backButton = new TextButton(
            bundle.get("screens.settings.buttons.back"),
            skin
        );
        defaults()
            .pad(Value.percentHeight(0.01f, this))
            .width(Value.percentWidth(0.3f, this));
        add(
            new Label(
                bundle.get("screens.settings.sliders.music.volume"),
                skin
            )
        );
        add(musicVolumeSlider).row();
        add(
            new Label(
                bundle.get("screens.settings.sliders.sound.volume"),
                skin
            )
        );
        add(soundVolumeSlider).row();
        add(backButton).colspan(2)
            .width(Value.percentWidth(0.6f, this));
    }

    public void onFinish(Runnable runnable) {
        backButton.addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    runnable.run();
                }
            }
        );
    }

    public void onMusicVolumeChange(Consumer<Float> consumer) {
        musicVolumeSlider.onVolumeChange(consumer);
    }

    public void onSoundVolumeChange(Consumer<Float> consumer) {
        soundVolumeSlider.onVolumeChange(consumer);
    }
}
