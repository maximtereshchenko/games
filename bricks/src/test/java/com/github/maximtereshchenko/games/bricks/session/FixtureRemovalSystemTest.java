package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class FixtureRemovalSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> remainingEntities =
        registry.view(new Query().all(Removed.class));
    private final World world = mock();
    private final Fixture fixture = mock();
    private final Body body = mock();
    private final FixtureRemovalSystem fixtureRemovalSystem =
        new FixtureRemovalSystem(registry, world);

    @BeforeEach
    void setUp() {
        registry.addSystems(fixtureRemovalSystem);
    }

    @Test
    void givenRemovedFixture_thenBodyDestroyedAndEntityDeleted() {
        when(fixture.getBody()).thenReturn(body);
        registry.addComponents(registry.createEntity(), Removed.INSTANCE, fixture);
        registry.update(0);
        assertThat(remainingEntities).isEmpty();
        verify(world).destroyBody(body);
    }
}
