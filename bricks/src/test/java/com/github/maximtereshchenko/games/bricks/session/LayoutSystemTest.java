package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.*;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class LayoutSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> brickEntities =
        registry.entities(new Query().all(Brick.class, Rectangle.class, WorldPosition.class, Color.class));
    private final Iterable<Entity> wallEntities =
        registry.entities(new Query().all(WorldPosition.class).none(Brick.class, Color.class));
    private final Configuration configuration = mock();
    private final Blueprints blueprints =
        new Blueprints.Builder(
            Map.of(
                BricksBlueprints.BRICK, List.of(Brick.INSTANCE),
                BricksBlueprints.WALL, List.of()
            )
        ).build();
    private final LayoutSystem layoutSystem = new LayoutSystem(
        registry,
        configuration,
        List.of(
            List.of(
                new BrickDefinition(Color.RED),
                new WallDefinition(),
                new EmptyCellDefinition()
            )
        ),
        blueprints
    );

    @BeforeEach
    void setUp() {
        when(configuration.worldDimensions()).thenReturn(new Configuration.Dimensions(30, 30));
        registry.addSystems(layoutSystem);
    }

    @Test
    void givenLayoutPolicy_thenBrickAndWallCreatedAndEmptySkipped() {
        registry.addComponents(registry.createEntity(), new LayoutPolicy(1, 1));
        registry.update(0);
        assertThat(brickEntities)
            .singleElement()
            .satisfies(entity -> {
                assertThat(entity.component(Rectangle.class).halfWidth).isEqualTo(4);
                assertThat(entity.component(WorldPosition.class).vector2())
                    .usingRecursiveComparison()
                    .isEqualTo(new Vector2(5, 25));
                assertThat(entity.component(Color.class)).isEqualTo(Color.RED);
            });
        assertThat(wallEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPosition.class).vector2())
            .usingRecursiveComparison()
            .isEqualTo(new Vector2(15, 25));
    }
}
