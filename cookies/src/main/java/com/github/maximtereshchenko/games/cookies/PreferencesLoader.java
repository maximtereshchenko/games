package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

final class PreferencesLoader
    extends AsynchronousAssetLoader<Preferences, AssetLoaderParameters<Preferences>> {

    private Preferences preferences;

    PreferencesLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(
        AssetManager manager,
        String fileName,
        FileHandle file,
        AssetLoaderParameters<Preferences> parameter
    ) {
        preferences = Gdx.app.getPreferences(fileName);
    }

    @Override
    public Preferences loadSync(
        AssetManager manager,
        String fileName,
        FileHandle file,
        AssetLoaderParameters<Preferences> parameter
    ) {
        var loaded = preferences;
        preferences = null;
        return loaded;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(
        String fileName,
        FileHandle file,
        AssetLoaderParameters<Preferences> parameter
    ) {
        return null;
    }
}
