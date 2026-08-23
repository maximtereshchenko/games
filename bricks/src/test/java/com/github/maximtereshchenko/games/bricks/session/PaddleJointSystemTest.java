package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class PaddleJointSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> jointEntities =
        registry.entities(new Query().all(WeldJoint.class));
    private final World world = mock();
    private final PhysicsObjectFactory physicsObjectFactory = mock();
    private final Fixture paddleFixture = mock();
    private final Fixture ballFixture = mock();
    private final Body paddleBody = mock();
    private final Body ballBody = mock();
    private final WeldJoint weldJoint = mock();
    private final PaddleJointSystem paddleJointSystem =
        new PaddleJointSystem(registry, world, physicsObjectFactory);

    @BeforeEach
    void setUp() {
        registry.addSystems(paddleJointSystem);
    }

    @Test
    void givenAttachedFixtureWithoutJoint_thenWeldJointCreated() {
        when(paddleFixture.getBody()).thenReturn(paddleBody);
        when(ballFixture.getBody()).thenReturn(ballBody);
        when(physicsObjectFactory.weldJoint(world, ballBody, paddleBody))
            .thenReturn(weldJoint);
        registry.addComponents(registry.createEntity(), Paddle.INSTANCE, paddleFixture);
        registry.addComponents(registry.createEntity(), Attached.INSTANCE, ballFixture);
        registry.update(0);
        assertThat(jointEntities)
            .singleElement()
            .extracting(entity -> entity.component(WeldJoint.class))
            .isSameAs(weldJoint);
    }
}
