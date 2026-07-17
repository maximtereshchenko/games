package com.github.maximtereshchenko.games.snakes.screen.view.main;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.snakes.Mode;
import com.github.maximtereshchenko.games.snakes.UserProfile;
import com.github.maximtereshchenko.games.snakes.screen.view.BasicButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ModesView extends Table {

    private final List<ModeButton> modeButtons;

    public ModesView(
        I18NBundle bundle,
        Skin skin,
        List<Mode> modes,
        UserProfile userProfile
    ) {
        modeButtons = new ArrayList<>();
        defaults().growX().uniformX().pad(3);
        var length = 4;
        for (var i = 0; i < modes.size(); i++) {
            var modeButton = new ModeButton(bundle, skin, modes.get(i), userProfile);
            modeButtons.add(modeButton);
            add(modeButton);
            if ((i + 1) % length == 0) {
                row();
            }
        }
        for (var i = 0; i < length - (modes.size() % length); i++) {
            add();
        }
    }

    public void onHover(Consumer<ModeButton> consumer) {
        setListeners(BasicButton::onHover, consumer);
    }

    public void onClick(Consumer<ModeButton> consumer) {
        setListeners(BasicButton::onClick, consumer);
    }

    private void setListeners(
        BiConsumer<BasicButton, Runnable> listener,
        Consumer<ModeButton> consumer
    ) {
        for (var modeButton : modeButtons) {
            listener.accept(modeButton, () -> consumer.accept(modeButton));
        }
    }
}
