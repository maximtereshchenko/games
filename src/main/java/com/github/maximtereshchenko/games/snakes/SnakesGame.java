package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Disposable;
import com.github.maximtereshchenko.games.snakes.event.*;
import com.github.maximtereshchenko.games.snakes.screen.ScreenFactory;
import com.github.maximtereshchenko.games.snakes.session.WorldDimensions;

import java.util.Set;

final class SnakesGame extends Game implements Subscriber {

    private final ScreenFactory screenFactory;
    private final WorldDimensions worldDimensions;
    private final Set<Disposable> disposables;
    private final UserProfile userProfile;

    SnakesGame(
        ScreenFactory screenFactory,
        WorldDimensions worldDimensions,
        Set<Disposable> disposables,
        UserProfile userProfile
    ) {
        this.screenFactory = screenFactory;
        this.worldDimensions = worldDimensions;
        this.disposables = disposables;
        this.userProfile = userProfile;
    }

    @Override
    public void create() {
        //empty
    }

    @Override
    public void dispose() {
        super.dispose();
        disposables.forEach(Disposable::dispose);
        userProfile.save();
    }

    @Override
    public void onEvent(ApplicationEvent event) {
        switch (event) {
            case AssetsLoaded _ -> setScreen(screenFactory.titleScreen());
            case TitleScreenFinished _, SnakeSessionEnded _ -> setScreen(
                screenFactory.modeSelectionScreen()
            );
            case ModeSelected modeSelected -> setScreen(
                screenFactory.snakeSessionScreen(worldDimensions, modeSelected.mode())
            );
        }
    }
}
