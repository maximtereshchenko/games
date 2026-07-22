package com.github.maximtereshchenko.games.snakes.configuration;

import com.github.maximtereshchenko.games.snakes.session.Position;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

final class PositionDeserializer extends StdDeserializer<Position> {

    PositionDeserializer() {
        super(Position.class);
    }

    @Override
    public Position deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext
    ) {
        var tree = jsonParser.<JsonNode>readValueAsTree();
        return new Position(
            tree.get("x").asInt(),
            tree.get("y").asInt()
        );
    }
}
