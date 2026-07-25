package com.github.maximtereshchenko.ecs;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.groups.Tuple.tuple;

final class WorldTest {

    @Test
    void givenEmptyWorld_thenEntityCreated() {
        var world = new World();
        var entityId = world.createEntity();
        assertThat(world.entities(new Query()))
            .singleElement()
            .extracting(Entity::id)
            .isEqualTo(entityId);
    }

    @Test
    void givenNonEmptyWorld_thenNewEntityCreated() {
        var world = new World();
        var firstEntityId = world.createEntity();
        var secondEntityId = world.createEntity();
        assertThat(firstEntityId).isNotEqualTo(secondEntityId);
        assertThat(world.entities(new Query()))
            .extracting(Entity::id)
            .containsExactly(firstEntityId, secondEntityId);
    }

    @Test
    void givenEntityExists_thenEntityDeleted() {
        var world = new World();
        world.deleteEntity(world.createEntity());
        assertThat(world.entities(new Query()))
            .isEmpty();
    }

    @Test
    void givenNoEntity_thenNothingDeleted() {
        var world = new World();
        world.deleteEntity(1);
        assertThat(world.entities(new Query()))
            .isEmpty();
    }

    @Test
    void givenEntityDeleted_thenEntityIdReused() {
        var world = new World();
        var entityId = world.createEntity();
        world.deleteEntity(entityId);
        assertThat(world.createEntity()).isEqualTo(entityId);
    }

    @Test
    void givenEntityDeleted_thenEntityIsNotVisibleInComponentQuery() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value");
        world.deleteEntity(entityId);
        assertThat(world.entities(new Query().all(String.class)))
            .isEmpty();
    }

    @Test
    void givenEntityDeletedTwice_thenEntityIdReusedOnce() {
        var world = new World();
        var entityId = world.createEntity();
        world.deleteEntity(entityId);
        world.deleteEntity(entityId);
        assertThat(world.createEntity()).isEqualTo(entityId);
        assertThat(world.createEntity()).isNotEqualTo(entityId);
    }

    @Test
    void givenOneOfTwoEntitiesInSameTableDeleted_thenOtherEntityRemainsAccessible() {
        var world = new World();
        var firstEntityId = world.createEntity();
        var secondEntityId = world.createEntity();
        world.addComponents(firstEntityId, "first");
        world.addComponents(secondEntityId, "second");
        world.deleteEntity(firstEntityId);
        assertThat(world.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(secondEntityId, "second");
    }

    @Test
    void givenMultipleEntitiesDeleted_thenEntityIdsReusedInOrder() {
        var world = new World();
        var firstEntityId = world.createEntity();
        var secondEntityId = world.createEntity();
        world.deleteEntity(firstEntityId);
        world.deleteEntity(secondEntityId);
        assertThat(world.createEntity()).isEqualTo(firstEntityId);
        assertThat(world.createEntity()).isEqualTo(secondEntityId);
    }

    @Test
    void givenEntityExists_thenMultipleComponentsRemovedAtOnce() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value", 1, 1.0);
        world.removeComponents(entityId, String.class, Integer.class);
        assertThat(world.entities(new Query().all(Double.class).none(String.class, Integer.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(Double.class)
            )
            .containsExactly(entityId, 1.0);
    }

    @Test
    void givenEntityExists_thenComponentsAdded() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value");
        assertThat(world.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "value");
    }

    @Test
    void givenNoEntity_thenAddComponentsThrowsException() {
        var world = new World();
        assertThatNullPointerException()
            .isThrownBy(() -> world.addComponents(1, ""));
    }

    @Test
    void givenEntityDeleted_thenAddComponentsThrowsException() {
        var world = new World();
        var entityId = world.createEntity();
        world.deleteEntity(entityId);
        assertThatNullPointerException()
            .isThrownBy(() -> world.addComponents(entityId, ""));
    }

    @Test
    void givenTwoEntitiesWithSameComponentTypes_thenComponentsUpdatedIndependently() {
        var world = new World();
        var firstEntityId = world.createEntity();
        var secondEntityId = world.createEntity();
        world.addComponents(firstEntityId, "first");
        world.addComponents(secondEntityId, "second");
        world.addComponents(firstEntityId, "updated");
        assertThat(world.entities(new Query().all(String.class)))
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(
                tuple(firstEntityId, "updated"),
                tuple(secondEntityId, "second")
            );
    }

    @Test
    void givenEntityExists_thenMultipleComponentsAddedAtOnce() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value", 1);
        assertThat(world.entities(new Query().all(String.class, Integer.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class),
                entity -> entity.component(Integer.class)
            )
            .containsExactly(entityId, "value", 1);
    }

    @Test
    void givenComponentAddedTwice_thenComponentValueIsOverwritten() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "first");
        world.addComponents(entityId, "second");
        assertThat(world.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "second");
    }

    @Test
    void givenTwoEntities_thenEachHoldsItsOwnComponentValue() {
        var world = new World();
        var firstEntityId = world.createEntity();
        var secondEntityId = world.createEntity();
        world.addComponents(firstEntityId, "first");
        world.addComponents(secondEntityId, "second");
        assertThat(world.entities(new Query().all(String.class)))
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(
                tuple(firstEntityId, "first"),
                tuple(secondEntityId, "second")
            );
    }

    @Test
    void givenEntityExists_thenComponentsRemoved() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value", 1);
        world.removeComponents(entityId, Integer.class);
        assertThat(world.entities(new Query().all(String.class).none(Integer.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "value");
    }

    @Test
    void givenNoEntity_thenRemoveComponentsThrowsException() {
        var world = new World();
        assertThatNullPointerException()
            .isThrownBy(() -> world.removeComponents(1, String.class));
    }

    @Test
    void givenAllComponentsRemoved_thenEntityAppearsInEmptyQueryOnly() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value");
        world.removeComponents(entityId, String.class);
        assertThat(world.entities(new Query().all(String.class)))
            .isEmpty();
        assertThat(world.entities(new Query()))
            .singleElement()
            .extracting(Entity::id)
            .isEqualTo(entityId);
    }

    @Test
    void givenComponentRemoved_thenOtherComponentsStillAccessible() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value", 1);
        world.removeComponents(entityId, Integer.class);
        assertThat(world.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "value");
    }

    @Test
    void givenRemovingNonExistentComponent_thenNothingRemoved() {
        var world = new World();
        var entityId = world.createEntity();
        world.removeComponents(entityId, String.class);
        assertThat(world.entities(new Query()))
            .singleElement()
            .extracting(Entity::id)
            .isEqualTo(entityId);
    }

    @Test
    void givenQueryWithAll_thenOnlyMatchingEntitiesReturned() {
        var world = new World();
        var firstEntityId = world.createEntity();
        var secondEntityId = world.createEntity();
        world.addComponents(firstEntityId, "value");
        world.addComponents(secondEntityId, 1);
        assertThat(world.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(firstEntityId, "value");
    }

    @Test
    void givenQueryWithAll_thenEntityWithSupersetOfComponentsIsReturned() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value", 1);
        assertThat(world.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class),
                entity -> entity.component(Integer.class)
            )
            .containsExactly(entityId, "value", 1);
    }

    @Test
    void givenQueryWithNone_thenEntitiesWithExcludedComponentAreExcluded() {
        var world = new World();
        var firstEntityId = world.createEntity();
        var secondEntityId = world.createEntity();
        world.addComponents(firstEntityId, "first");
        world.addComponents(secondEntityId, "second", 1);
        assertThat(world.entities(new Query().none(Integer.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(firstEntityId, "first");
    }

    @Test
    void givenQueryWithNone_thenEntityWithSupersetContainingExcludedTypeIsExcluded() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value", 1);
        assertThat(world.entities(new Query().all(String.class).none(Integer.class)))
            .isEmpty();
    }

    @Test
    void givenQueryWithOne_thenEntityWithAtLeastOneMatchingComponentReturned() {
        var world = new World();
        var firstEntityId = world.createEntity();
        var secondEntityId = world.createEntity();
        var thirdEntityId = world.createEntity();
        world.addComponents(firstEntityId, "value");
        world.addComponents(secondEntityId, 1);
        world.addComponents(thirdEntityId, new Object());
        assertThat(world.entities(new Query().one(String.class, Integer.class)))
            .satisfiesExactly(
                entity -> {
                    assertThat(entity.id()).isEqualTo(firstEntityId);
                    assertThat(entity.component(String.class)).isEqualTo("value");
                },
                entity -> {
                    assertThat(entity.id()).isEqualTo(secondEntityId);
                    assertThat(entity.component(Integer.class)).isOne();
                }
            );
    }

    @Test
    void givenQueryWithAllOneNone_thenCombinedFilterApplied() {
        var world = new World();
        var firstEntityId = world.createEntity();
        var secondEntityId = world.createEntity();
        var thirdEntityId = world.createEntity();
        world.addComponents(firstEntityId, "first", 1);
        world.addComponents(secondEntityId, "second", 1.0);
        world.addComponents(thirdEntityId, "third", new Object());
        assertThat(
            world.entities(
                new Query()
                    .all(String.class)
                    .one(Integer.class, Double.class)
                    .none(Object.class))
        )
            .satisfiesExactly(
                entity -> {
                    assertThat(entity.id()).isEqualTo(firstEntityId);
                    assertThat(entity.component(String.class)).isEqualTo("first");
                    assertThat(entity.component(Integer.class)).isOne();
                },
                entity -> {
                    assertThat(entity.id()).isEqualTo(secondEntityId);
                    assertThat(entity.component(String.class)).isEqualTo("second");
                    assertThat(entity.component(Double.class)).isOne();
                }
            );
    }

    @Test
    void givenQueryWithOneSingleType_thenBehavesLikeAll() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value");
        assertThat(world.entities(new Query().one(String.class)))
            .hasSameElementsAs(world.entities(new Query().all(String.class)));
    }

    @Test
    void givenReusedEntityId_thenPreviousComponentsNotVisible() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value");
        world.deleteEntity(entityId);
        world.addComponents(world.createEntity(), 1);
        assertThat(world.entities(new Query().all(Integer.class).none(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(Integer.class)
            )
            .containsExactly(entityId, 1);
    }

    @Test
    void givenEntityMovedAcrossTables_thenQueryReflectsCurrentState() {
        var world = new World();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value");
        assertThat(world.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "value");
        world.addComponents(entityId, 1);
        assertThat(world.entities(new Query().all(String.class, Integer.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class),
                entity -> entity.component(Integer.class)
            )
            .containsExactly(entityId, "value", 1);
        world.removeComponents(entityId, String.class);
        assertThat(world.entities(new Query().all(Integer.class).none(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(Integer.class)
            )
            .containsExactly(entityId, 1);
    }

    @Test
    void givenEntityWithComponent_thenAccessingAbsentComponentThrowsException() {
        var world = new World();
        var entityId = world.createEntity();
        assertThat(world.entities(new Query()))
            .singleElement()
            .satisfies(
                entity -> {
                    assertThat(entity.id()).isEqualTo(entityId);
                    assertThatIllegalArgumentException()
                        .isThrownBy(() -> entity.component(String.class));
                }
            );
    }

    @Test
    void givenSameQuery_thenSameView() {
        var world = new World();
        var first = world.entities(new Query());
        var second = world.entities(new Query());
        assertThat(first).isSameAs(second);
    }

    @Test
    void givenWorldUpdatedAfterViewCreated_thenViewContainsNewEntities() {
        var world = new World();
        var entities = world.entities(new Query().all(String.class));
        assertThat(entities).isEmpty();
        var entityId = world.createEntity();
        world.addComponents(entityId, "value");
        assertThat(entities)
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "value");
    }

    @Test
    void givenSystem_thenSystemRunsOnUpdate() {
        var deltaTimeFromSystem = new AtomicReference<>();
        var world = new World();
        world.addSystems(
            (_, deltaTimeSeconds) -> deltaTimeFromSystem.set(deltaTimeSeconds)
        );
        world.update(12.34f);
        assertThat(deltaTimeFromSystem).hasValue(12.34f);
    }

    @Test
    void givenSystemAddedComponents_thenNextSystemSeeChanges() {
        var world = new World();
        var entities = world.entities(new Query().all(String.class));
        world.addSystems(
            (worldEdit, _) -> worldEdit.addComponents(
                worldEdit.createEntity(),
                "value"
            ),
            (_, _) -> assertThat(entities)
                .singleElement()
                .extracting(entity -> entity.component(String.class))
                .isEqualTo("value")
        );
        world.update(1);
    }

    @Test
    void givenSystemRemovedComponents_thenNextSystemSeeChanges() {
        var world = new World();
        world.addComponents(world.createEntity(), "value");
        var entities = world.entities(new Query().all(String.class));
        world.addSystems(
            (worldEdit, _) -> {
                for (var entity : entities) {
                    worldEdit.removeComponents(entity.id(), String.class);
                }
                assertThat(entities)
                    .singleElement()
                    .extracting(entity -> entity.component(String.class))
                    .isEqualTo("value");
            },
            (_, _) -> assertThat(entities).isEmpty()
        );
        world.update(1);
    }

    @Test
    void givenSystemDeletedEntity_thenNextSystemSeeChanges() {
        var world = new World();
        world.addComponents(world.createEntity(), "value");
        var entities = world.entities(new Query());
        world.addSystems(
            (worldEdit, _) -> {
                for (var entity : entities) {
                    worldEdit.deleteEntity(entity.id());
                }
                assertThat(entities)
                    .singleElement()
                    .extracting(entity -> entity.component(String.class))
                    .isEqualTo("value");
            },
            (_, _) -> assertThat(entities).isEmpty()
        );
        world.update(1);
    }
}