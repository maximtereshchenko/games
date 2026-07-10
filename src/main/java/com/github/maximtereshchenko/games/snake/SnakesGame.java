package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

final class SnakesGame extends Game implements Subscriber {

    private final Screen titleScreen;
    private final Screen modeSelectionScreen;
    private final Screen snakeSessionScreen;
    private final Disposables disposables;

    SnakesGame(
        Screen loadingScreen,
        Screen titleScreen,
        Screen modeSelectionScreen,
        Screen snakeSessionScreen,
        Disposables disposables
    ) {
        this.titleScreen = titleScreen;
        this.modeSelectionScreen = modeSelectionScreen;
        this.snakeSessionScreen = snakeSessionScreen;
        this.disposables = disposables;
        setScreen(loadingScreen);
    }

    @Override
    public void create() {
        //empty
    }

    @Override
    public void dispose() {
        super.dispose();
        disposables.dispose();
    }

    @Override
    public void onEvent(ApplicationEvent event) {
        switch (event) {
            case AssetsLoaded _ -> setScreen(titleScreen);
            case TitleScreenFinished _, SnakeSessionEnded _ -> setScreen(modeSelectionScreen);
            case ModeSelected _ -> setScreen(snakeSessionScreen);
        }
    }
}
