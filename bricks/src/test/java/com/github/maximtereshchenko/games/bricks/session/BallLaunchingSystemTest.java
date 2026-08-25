package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class BallLaunchingSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> jointEntities =
        registry.view(new Query().all(WeldJoint.class));
    private final World world = mock();
    private final WeldJoint weldJoint = mock();
    private final BallLaunchingSystem ballLaunchingSystem =
        new BallLaunchingSystem(registry, world);

    @BeforeEach
    void setUp() {
        registry.addSystems(ballLaunchingSystem);
    }

    @Test
    void givenWeldJointWithoutAttached_thenJointDestroyedAndVelocitySet() {
        var velocity = new Velocity(new Vector2());
        registry.addComponents(
            registry.createEntity(),
            weldJoint,
            new Speed(7),
            velocity
        );
        registry.update(0);
        assertThat(jointEntities).isEmpty();
        assertThat(velocity.vector2().y).isEqualTo(7);
        verify(world).destroyJoint(weldJoint);
    }
}
