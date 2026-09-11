package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Configuration;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.ScreenFactory;

import java.util.Set;

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
        assetManager.setLoader(
            Skin.class,
            new CookiesSkinLoader(fileHandleResolver)
        );
        assetManager.setLoader(
            Configuration.class,
            new ConfigurationLoader(fileHandleResolver)
        );
        cookiesGame = new CookiesGame(
            Set.of(spriteBatch, assetManager)
        );
        var screenFactory = new ScreenFactory(
            spriteBatch,
            assetManager,
            new Assets(
                new Assets.Loading(
                    new AssetDescriptor<>(
                        "loading.json",
                        Skin.class
                    ),
                    new AssetDescriptor<>(
                        "loading",
                        I18NBundle.class
                    )
                ),
                new Assets.Game(
                    new AssetDescriptor<>(
                        "configuration.json",
                        Configuration.class
                    ),
                    new AssetDescriptor<>(
                        "game.json",
                        Skin.class
                    ),
                    new AssetDescriptor<>(
                        "game",
                        I18NBundle.class
                    )
                )
            ),
            cookiesGame
        );
        cookiesGame.setScreen(screenFactory.loadingScreen());
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
