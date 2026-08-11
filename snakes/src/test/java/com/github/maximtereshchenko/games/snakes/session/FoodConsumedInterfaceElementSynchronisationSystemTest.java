package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodConsumedInterfaceElementSynchronisationSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> localizableInterfaceTextEntities =
        registry.entities(
            new Query()
                .all(FoodConsumedInterfaceElement.class, LocalizableInterfaceText.class)
        );
    private final FoodConsumedInterfaceElementSynchronisationSystem system =
        new FoodConsumedInterfaceElementSynchronisationSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(system);
    }

    @Test
    void whenUpdated_thenFoodConsumedAddedToVariables() {
        registry.addComponents(
            registry.createEntity(),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 4))
        );
        registry.addComponents(
            registry.createEntity(),
            FoodConsumedInterfaceElement.INSTANCE,
            new LocalizableInterfaceText(
                "screens.session.food.consumed.template",
                new ArrayList<>()
            )
        );
        registry.update(0);
        assertThat(localizableInterfaceTextEntities)
            .singleElement()
            .extracting(entity -> entity.component(LocalizableInterfaceText.class).variables())
            .isEqualTo(List.of(4));
    }
}
