package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class PhysicsSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> collisionEntities =
        registry.view(new Query().all(Collisions.class));
    private final World world = mock();
    private final Fixture first = mock();
    private final Fixture second = mock();
    private final Contact contact = mock();
    private final PhysicsSystem physicsSystem = new PhysicsSystem(registry, world);

    @BeforeEach
    void setUp() {
        when(contact.getFixtureA()).thenReturn(first);
        when(contact.getFixtureB()).thenReturn(second);
        registry.addSystems(physicsSystem);
    }

    @Test
    void givenContactWithUserData_thenCollisionsAdded() {
        var firstId = registry.createEntity();
        var secondId = registry.createEntity();
        when(first.getUserData()).thenReturn(firstId);
        when(second.getUserData()).thenReturn(secondId);
        registry.addComponents(
            firstId,
            new PhysicsPolicy(0.016f, 0.1f, 0)
        );
        physicsSystem.beginContact(contact);
        registry.update(0.016f);
        assertThat(collisionEntities).hasSize(2);
        assertThat(registry.view(new Query().all(Collisions.class)))
            .allSatisfy(entity ->
                assertThat(entity.component(Collisions.class).entityIds()).isNotEmpty()
            );
        verify(world).step(0.016f, 8, 3);
    }

    @Test
    void givenMissingUserData_thenCollisionsNotAdded() {
        physicsSystem.beginContact(contact);
        registry.update(0);
        assertThat(collisionEntities).isEmpty();
    }
}
