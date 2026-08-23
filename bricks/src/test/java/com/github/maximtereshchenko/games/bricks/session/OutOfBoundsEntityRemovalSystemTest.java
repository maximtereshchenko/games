package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class OutOfBoundsEntityRemovalSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> removedEntities =
        registry.entities(new Query().all(Removed.class));
    private final Configuration configuration = mock();
    private final OutOfBoundsEntityRemovalSystem outOfBoundsEntityRemovalSystem =
        new OutOfBoundsEntityRemovalSystem(registry, configuration);

    @BeforeEach
    void setUp() {
        when(configuration.worldDimensions()).thenReturn(new Configuration.Dimensions(10, 10));
        registry.addSystems(outOfBoundsEntityRemovalSystem);
    }

    @Test
    void givenPositionInsideMargin_thenNotRemoved() {
        registry.addComponents(registry.createEntity(), new WorldPosition(new Vector2(0, 10)));
        registry.update(0);
        assertThat(removedEntities).isEmpty();
    }

    @Test
    void givenPositionOutsideMargin_thenRemoved() {
        registry.addComponents(registry.createEntity(), new WorldPosition(new Vector2(-1, 5)));
        registry.update(0);
        assertThat(removedEntities).hasSize(1);
    }
}
