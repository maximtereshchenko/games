package com.github.maximtereshchenko.games.snakes.configuration;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.github.maximtereshchenko.games.snakes.session.Position;
import tools.jackson.databind.module.SimpleModule;

public final class ConfigurationModule extends SimpleModule {

    public ConfigurationModule() {
        addDeserializer(Position.class, new PositionDeserializer());
        addDeserializer(AssetDescriptor.class, new AssetDescriptorDeserializer(null));
    }
}
