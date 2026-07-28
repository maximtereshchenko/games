package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class AirSupplyInterfaceElementSynchronisationSystemTest {

    private final World world = new World();
    private final Iterable<Entity> interfaceTextEntities =
        world.entities(
            new Query().all(AirSupplyInterfaceElement.class, InterfaceText.class)
        );
    private final I18NBundle bundle = mock();
    private final AirSupplyInterfaceElementSynchronisationSystem system =
        new AirSupplyInterfaceElementSynchronisationSystem(world, bundle);

    @BeforeEach
    void setUp() {
        world.addSystems(system);
        when(bundle.get("screens.session.air.template")).thenReturn("Air: %d");
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new AirSupply(5, 3));
        world.addComponents(
            world.createEntity(),
            AirSupplyInterfaceElement.INSTANCE,
            new InterfaceText(1, "Air: 0")
        );
        world.update(0);
        assertThat(interfaceTextEntities)
            .singleElement()
            .extracting(entity -> entity.component(InterfaceText.class).value)
            .isEqualTo("Air: 0");
    }

    @Test
    void givenTurnStartedEvent_thenInterfaceTextSynchronised() {
        world.addComponents(world.createEntity(), new AirSupply(5, 3));
        world.addComponents(
            world.createEntity(),
            AirSupplyInterfaceElement.INSTANCE,
            new InterfaceText(1, "Air: 0")
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(interfaceTextEntities)
            .singleElement()
            .extracting(entity -> entity.component(InterfaceText.class).value)
            .isEqualTo("Air: 3");
    }
}
