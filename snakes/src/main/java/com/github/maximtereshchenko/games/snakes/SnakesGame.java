package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Disposable;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.snakes.event.*;
import com.github.maximtereshchenko.games.snakes.screen.ScreenFactory;

import java.util.Set;

final class SnakesGame extends Game implements Subscriber<ApplicationEvent> {

    private final ScreenFactory screenFactory;
    private final Set<Disposable> disposables;
    private final UserProfile userProfile;

    SnakesGame(
        ScreenFactory screenFactory,
        Set<Disposable> disposables,
        UserProfile userProfile
    ) {
        this.screenFactory = screenFactory;
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
            case AssetsLoaded _ -> setScreen(
                screenFactory.titleScreen()
            );
            case TitleScreenFinished _,
                 SessionEnded _,
                 StatisticsScreenFinished _,
                 CreditsScreenFinished _,
                 SettingsScreenFinished _ -> setScreen(
                screenFactory.mainScreen()
            );
            case ModeSelected modeSelected -> setScreen(
                screenFactory.snakeSessionScreen(modeSelected.mode())
            );
            case StatisticsRequested _ -> setScreen(
                screenFactory.statisticsScreen()
            );
            case SettingsRequested _ -> setScreen(
                screenFactory.settingsScreen()
            );
            case CreditsRequested _ -> setScreen(
                screenFactory.creditsScreen()
            );
        }
    }
}
