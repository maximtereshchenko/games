package com.github.maximtereshchenko.games.common.configuration;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

final class EnumDeserializer<T extends Enum<T>> extends StdDeserializer<T> {

    EnumDeserializer(JavaType valueType) {
        super(valueType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(JsonParser parser, DeserializationContext context) {
        return Enum.valueOf(
            (Class<T>) _valueType.getRawClass(),
            name(parser.readValueAsTree())
        );
    }

    private String name(JsonNode node) {
        if (node.isString()) {
            return node.asString();
        }
        return node.get("value").asString();
    }
}
