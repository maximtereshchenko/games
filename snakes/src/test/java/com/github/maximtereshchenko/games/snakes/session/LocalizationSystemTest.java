package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class LocalizationSystemTest {

    private final World world = new World();
    private final Iterable<Entity> interfaceTextEntities =
        world.entities(new Query().all(InterfaceText.class));
    private final I18NBundle bundle = mock();
    private final LocalizationSystem localizationSystem =
        new LocalizationSystem(world, bundle);

    @BeforeEach
    void setUp() {
        world.addSystems(localizationSystem);
    }

    @Test
    void whenUpdated_thenInterfaceTextFormatted() {
        when(bundle.format("screens.session.air.template", 3))
            .thenReturn("AIR: 3");
        world.addComponents(
            world.createEntity(),
            new LocalizableInterfaceText(
                "screens.session.air.template",
                List.of(3)
            ),
            new InterfaceText(1, "")
        );
        world.update(0);
        assertThat(interfaceTextEntities)
            .singleElement()
            .extracting(entity -> entity.component(InterfaceText.class).value)
            .isEqualTo("AIR: 3");
    }
}
