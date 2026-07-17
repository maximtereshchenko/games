package com.github.maximtereshchenko.games.snakes.screen.view.main;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.snakes.Mode;
import com.github.maximtereshchenko.games.snakes.UserProfile;
import com.github.maximtereshchenko.games.snakes.screen.view.BasicButton;

public final class ModeButton extends BasicButton {

    private final Mode mode;

    ModeButton(
        I18NBundle bundle,
        Skin skin,
        Mode mode,
        UserProfile userProfile
    ) {
        super(bundle.get("modes.%s.name".formatted(mode.name())), skin);
        this.mode = mode;
        setDisabled(!userProfile.isUnlocked(mode));
    }

    public Mode mode() {
        return mode;
    }
}
