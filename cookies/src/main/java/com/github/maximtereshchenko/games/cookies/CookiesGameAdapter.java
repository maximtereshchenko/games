package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.maximtereshchenko.games.common.configuration.ConfigurationReader;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.screen.StageScreen;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.screen.BakeryScreen;
import com.github.maximtereshchenko.games.cookies.screen.view.BakeryView;
import tools.jackson.core.type.TypeReference;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

final class CookiesGameAdapter implements ApplicationListener {

    private CookiesGame cookiesGame;

    static void main() {
        var displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        var configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setWindowedMode(
            displayMode.width,
            displayMode.height
        );
        new Lwjgl3Application(
            new CookiesGameAdapter(),
            configuration
        );
    }

    @Override
    public void create() {
        var spriteBatch = new SpriteBatch();
        var fileHandleResolver = new ClasspathFileHandleResolver();
        var assetManager = new AssetManager(fileHandleResolver);
        var skinAssetDescriptor = new AssetDescriptor<>("skin.json", Skin.class);
        var gameBundleAssetDescriptor = new AssetDescriptor<>("game", I18NBundle.class);
        assetManager.load(skinAssetDescriptor);
        assetManager.load(gameBundleAssetDescriptor);
        assetManager.finishLoading();
        var eventBus = new EventBus<Event>();
        var bakeryService = new BakeryService(
            new ConfigurationReader()
                .value(
                    "configuration.json",
                    new TypeReference<>() {}
                ),
            eventBus
        );
        var skin = assetManager.get(skinAssetDescriptor);
        var random = ThreadLocalRandom.current();
        var stage = new Stage(new ScreenViewport(), spriteBatch);
        stage.addActor(
            new BakeryView(
                skin,
                assetManager.get(gameBundleAssetDescriptor),
                random,
                bakeryService,
                eventBus
            )
        );
        bakeryService.onStart();
        cookiesGame = new CookiesGame(Set.of(spriteBatch));
        cookiesGame.setScreen(
            new BakeryScreen(
                new StageScreen(stage),
                bakeryService
            )
        );
    }

    @Override
    public void resize(int width, int height) {
        cookiesGame.resize(width, height);
    }

    @Override
    public void render() {
        cookiesGame.render();
    }

    @Override
    public void pause() {
        cookiesGame.pause();
    }

    @Override
    public void resume() {
        cookiesGame.resume();
    }

    @Override
    public void dispose() {
        cookiesGame.dispose();
    }
}
