package com.github.maximtereshchenko.snakes.screen.view.settings;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.function.Consumer;

final class MusicVolumeSlider extends Slider {

    MusicVolumeSlider(Skin skin, float volume) {
        super(0, 1, 0.1f, false, skin);
        setValue(volume);
    }

    void onVolumeChange(Consumer<Float> consumer) {
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    consumer.accept(getValue());
                }
            }
        );
    }
}
