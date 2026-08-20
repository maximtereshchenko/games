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
    private final Set<Disposable> disposables;

    BricksGame(
        ScreenFactory screenFactory,
        Set<Disposable> disposables
    ) {
        this.screenFactory = screenFactory;
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
                case AssetsLoaded _ -> screenFactory.mainScreen();
                case LevelCompleted _ -> screenFactory.mainScreen();
                case LevelFailed _ -> screenFactory.mainScreen();
                case DifficultySelected difficultySelected -> screenFactory.levelSelectionScreen(
                    difficultySelected.name()
                );
                case LevelSelected levelSelected -> screenFactory.sessionScreen(
                    levelSelected.difficulty(),
                    levelSelected.level()
                );
                case DifficultySelectionRequested _ -> screenFactory.difficultySelectionScreen();
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
