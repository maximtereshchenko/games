package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.github.maximtereshchenko.games.bricks.event.*;
import com.github.maximtereshchenko.games.bricks.screen.ScreenFactory;
import com.github.maximtereshchenko.games.event.Subscriber;

import java.util.Set;

final class BricksGame extends Game implements Subscriber<Event> {

    private final ScreenFactory screenFactory;
    private final UserProfile userProfile;
    private final Set<Disposable> disposables;

    BricksGame(
        ScreenFactory screenFactory,
        UserProfile userProfile,
        Set<Disposable> disposables
    ) {
        this.screenFactory = screenFactory;
        this.userProfile = userProfile;
        this.disposables = disposables;
    }

    @Override
    public void create() {
        //empty
    }

    @Override
    public void dispose() {
        disposeScreen();
        disposables.forEach(Disposable::dispose);
        userProfile.save();
    }

    @Override
    public void setScreen(Screen screen) {
        disposeScreen();
        super.setScreen(screen);
    }

    @Override
    public void onEvent(Event event) {
        setScreen(
            switch (event) {
                case AssetsLoaded _,
                     LevelCompleted _,
                     LevelFailed _,
                     SettingsScreenFinished _ -> screenFactory.mainScreen();
                case DifficultySelected difficultySelected -> screenFactory.levelSelectionScreen(
                    difficultySelected.name()
                );
                case LevelSelected levelSelected -> screenFactory.sessionScreen(
                    levelSelected.difficulty(),
                    levelSelected.level()
                );
                case DifficultySelectionRequested _ -> screenFactory.difficultySelectionScreen();
                case SettingsRequested _ -> screenFactory.settingsScreen();
            }
        );
    }

    private void disposeScreen() {
        var current = getScreen();
        if (current != null) {
            current.dispose();
        }
    }
}
