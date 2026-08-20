package com.github.maximtereshchenko.games.bricks.configuration;

import com.badlogic.gdx.assets.AssetDescriptor;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.std.StdDeserializer;

import java.util.Objects;

public final class AssetDescriptorDeserializer
    extends StdDeserializer<AssetDescriptor<?>> {

    private final JavaType javaType;

    public AssetDescriptorDeserializer() {
        this(null);
    }

    private AssetDescriptorDeserializer(JavaType javaType) {
        super(AssetDescriptor.class);
        this.javaType = javaType;
    }

    @Override
    public ValueDeserializer<?> createContextual(
        DeserializationContext deserializationContext,
        BeanProperty beanProperty
    ) {
        return new AssetDescriptorDeserializer(
            beanProperty.getType()
                .containedType(0)
        );
    }

    @Override
    public AssetDescriptor<?> deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext
    ) {
        Objects.requireNonNull(javaType);
        return new AssetDescriptor<>(
            jsonParser.getValueAsString(),
            javaType.getRawClass()
        );
    }
}
