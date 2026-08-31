package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.maximtereshchenko.games.common.screen.StageScreen;

import java.util.Set;

final class CookiesGameAdapter implements ApplicationListener {

    private CookiesGame cookiesGame;

    static void main() {
        new Lwjgl3Application(new CookiesGameAdapter());
    }

    @Override
    public void create() {
        var spriteBatch = new SpriteBatch();
        var fileHandleResolver = new ClasspathFileHandleResolver();
        var assetManager = new AssetManager(fileHandleResolver);
        var skin = new AssetDescriptor<>("skin.json", Skin.class);
        assetManager.load(skin);
        assetManager.finishLoading();
        var stage = new Stage(new ScreenViewport(), spriteBatch);
        stage.addActor(new CookiesView(assetManager.get(skin)));
        cookiesGame = new CookiesGame(Set.of(spriteBatch));
        cookiesGame.setScreen(new StageScreen(stage));
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
