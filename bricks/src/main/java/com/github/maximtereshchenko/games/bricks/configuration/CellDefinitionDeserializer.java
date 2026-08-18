package com.github.maximtereshchenko.games.bricks.configuration;

import com.badlogic.gdx.graphics.Color;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

public final class CellDefinitionDeserializer
    extends StdDeserializer<CellDefinition> {

    public CellDefinitionDeserializer() {
        super(CellDefinition.class);
    }

    @Override
    public CellDefinition deserialize(
        JsonParser jsonParser,
        DeserializationContext context
    ) throws JacksonException {
        var jsonNode = jsonParser.<JsonNode>readValueAsTree();
        return switch (jsonNode.get("type").asString()) {
            case BrickDefinition.TYPE -> new BrickDefinition(
                context.readTreeAsValue(
                    jsonNode.get("color"),
                    Color.class
                )
            );
            case WallDefinition.TYPE -> new WallDefinition();
            case EmptyCellDefinition.TYPE -> new EmptyCellDefinition();
            default -> throw new IllegalArgumentException();
        };
    }
}
