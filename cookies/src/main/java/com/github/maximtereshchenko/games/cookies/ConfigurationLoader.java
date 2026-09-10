package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.maximtereshchenko.games.common.configuration.ConfigurationReader;
import com.github.maximtereshchenko.games.cookies.domain.Configuration;
import tools.jackson.core.type.TypeReference;

final class ConfigurationLoader
    extends AsynchronousAssetLoader<Configuration, AssetLoaderParameters<Configuration>> {

    private final ConfigurationReader configurationReader;
    private Configuration configuration;

    ConfigurationLoader(FileHandleResolver resolver) {
        super(resolver);
        configurationReader = new ConfigurationReader();
    }

    @Override
    public void loadAsync(
        AssetManager manager,
        String fileName,
        FileHandle file,
        AssetLoaderParameters<Configuration> parameter
    ) {
        configuration = configurationReader.value(
            file,
            new TypeReference<>() {}
        );
    }

    @Override
    public Configuration loadSync(
        AssetManager manager,
        String fileName,
        FileHandle file,
        AssetLoaderParameters<Configuration> parameter
    ) {
        var loaded = configuration;
        configuration = null;
        return loaded;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(
        String fileName,
        FileHandle file,
        AssetLoaderParameters<Configuration> parameter
    ) {
        return null;
    }
}
