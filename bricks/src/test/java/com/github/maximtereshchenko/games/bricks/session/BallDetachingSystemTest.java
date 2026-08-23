package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class BallDetachingSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> attachedEntities =
        registry.entities(new Query().all(Attached.class));
    private final Input input = mock();
    private final BallDetachingSystem ballDetachingSystem =
        new BallDetachingSystem(registry);

    @BeforeEach
    void setUp() {
        Gdx.input = input;
        registry.addSystems(ballDetachingSystem);
    }

    @Test
    void givenLeftButtonJustPressed_thenAttachedRemoved() {
        when(input.isButtonJustPressed(Input.Buttons.LEFT)).thenReturn(true);
        registry.addComponents(registry.createEntity(), Ball.INSTANCE, Attached.INSTANCE);
        registry.update(0);
        assertThat(attachedEntities).isEmpty();
    }

    @Test
    void givenLeftButtonNotPressed_thenAttachedKept() {
        when(input.isButtonJustPressed(Input.Buttons.LEFT)).thenReturn(false);
        registry.addComponents(registry.createEntity(), Ball.INSTANCE, Attached.INSTANCE);
        registry.update(0);
        assertThat(attachedEntities).hasSize(1);
    }
}
