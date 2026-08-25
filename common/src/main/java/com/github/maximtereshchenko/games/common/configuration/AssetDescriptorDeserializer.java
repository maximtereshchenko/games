package com.github.maximtereshchenko.games.common.configuration;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.*;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Objects;

final class AssetDescriptorDeserializer extends StdDeserializer<AssetDescriptor<?>> {

    private final JavaType javaType;

    AssetDescriptorDeserializer() {
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
    @SuppressWarnings({"unchecked", "rawtypes"})
    public AssetDescriptor<?> deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext
    ) {
        Objects.requireNonNull(javaType);
        var tree = jsonParser.<JsonNode>readValueAsTree();
        var type = javaType.getRawClass();
        return new AssetDescriptor(
            tree.get("fileName").asString(),
            type,
            assetLoaderParameters(
                type,
                tree.get("params"),
                deserializationContext,
                jsonParser
            )
        );
    }

    private AssetLoaderParameters<?> assetLoaderParameters(
        Class<?> type,
        JsonNode jsonNode,
        DeserializationContext deserializationContext,
        JsonParser jsonParser
    ) {
        if (jsonNode == null) {
            return null;
        }
        if (type == BitmapFont.class) {
            return deserializationContext.readTreeAsValue(
                jsonNode,
                BitmapFontLoader.BitmapFontParameter.class
            );
        }
        throw new InvalidFormatException(
            jsonParser,
            "Could not find suitable AssetLoaderParameters for " + type,
            jsonNode,
            type
        );
    }
}
