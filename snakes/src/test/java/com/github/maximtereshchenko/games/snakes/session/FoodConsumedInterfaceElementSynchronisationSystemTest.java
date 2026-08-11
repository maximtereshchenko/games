package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodConsumedInterfaceElementSynchronisationSystemTest {

    private final World world = new World();
    private final Iterable<Entity> localizableInterfaceTextEntities =
        world.entities(
            new Query()
                .all(FoodConsumedInterfaceElement.class, LocalizableInterfaceText.class)
        );
    private final FoodConsumedInterfaceElementSynchronisationSystem system =
        new FoodConsumedInterfaceElementSynchronisationSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(system);
    }

    @Test
    void whenUpdated_thenFoodConsumedAddedToVariables() {
        world.addComponents(
            world.createEntity(),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 4))
        );
        world.addComponents(
            world.createEntity(),
            FoodConsumedInterfaceElement.INSTANCE,
            new LocalizableInterfaceText(
                "screens.session.food.consumed.template",
                new ArrayList<>()
            )
        );
        world.update(0);
        assertThat(localizableInterfaceTextEntities)
            .singleElement()
            .extracting(entity -> entity.component(LocalizableInterfaceText.class).variables())
            .isEqualTo(List.of(4));
    }
}
