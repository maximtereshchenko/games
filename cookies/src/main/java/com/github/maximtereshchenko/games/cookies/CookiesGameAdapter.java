package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Configuration;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.ScreenFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

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
        assetManager.setLoader(
            Preferences.class,
            new PreferencesLoader(fileHandleResolver)
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
                        "com.github.maximtereshchenko.games.cookies.domain.PlayerProgress",
                        Preferences.class
                    ),
                    new AssetDescriptor<>(
                        "game.json",
                        Skin.class
                    ),
                    new AssetDescriptor<>(
                        "game",
                        I18NBundle.class
                    ),
                    sounds(7, "sounds/cookie_%d.mp3"),
                    sounds(4, "sounds/building_%d.mp3"),
                    new AssetDescriptor<>(
                        "sounds/tick.mp3",
                        Sound.class
                    ),
                    new AssetDescriptor<>(
                        "sounds/menu-on.mp3",
                        Sound.class
                    ),
                    new AssetDescriptor<>(
                        "sounds/menu-off.mp3",
                        Sound.class
                    ),
                    new AssetDescriptor<>(
                        "sounds/golden-cookie-spawn.mp3",
                        Sound.class
                    ),
                    new AssetDescriptor<>(
                        "sounds/golden-cookie-consume.mp3",
                        Sound.class
                    )
                )
            ),
            cookiesGame,
            ThreadLocalRandom.current()
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

    private List<AssetDescriptor<Sound>> sounds(int max, String pattern) {
        return IntStream.range(0, max)
            .mapToObj(pattern::formatted)
            .map(name -> new AssetDescriptor<>(name, Sound.class))
            .toList();
    }
}
