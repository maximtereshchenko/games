package com.github.maximtereshchenko.games.common.configuration;

import com.badlogic.gdx.assets.AssetDescriptor;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.module.SimpleDeserializers;

public final class ConfigurationDeserializers extends SimpleDeserializers {

    public ConfigurationDeserializers() {
        addDeserializer(AssetDescriptor.class, new AssetDescriptorDeserializer());
    }

    @Override
    public ValueDeserializer<?> findEnumDeserializer(
        JavaType enumType,
        DeserializationConfig config,
        BeanDescription.Supplier beanDescRef
    ) {
        return new EnumDeserializer<>(enumType);
    }
}
