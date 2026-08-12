package com.github.maximtereshchenko.games.ecs;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.groups.Tuple.tuple;

final class RegistryTest {

    @Test
    void givenEmptyRegistry_thenEntityCreated() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        assertThat(registry.entities(new Query()))
            .singleElement()
            .extracting(Entity::id)
            .isEqualTo(entityId);
    }

    @Test
    void givenNonEmptyRegistry_thenNewEntityCreated() {
        var registry = new Registry();
        var firstEntityId = registry.createEntity();
        var secondEntityId = registry.createEntity();
        assertThat(firstEntityId).isNotEqualTo(secondEntityId);
        assertThat(registry.entities(new Query()))
            .extracting(Entity::id)
            .containsExactly(firstEntityId, secondEntityId);
    }

    @Test
    void givenEntityExists_thenEntityDeleted() {
        var registry = new Registry();
        registry.deleteEntity(registry.createEntity());
        assertThat(registry.entities(new Query()))
            .isEmpty();
    }

    @Test
    void givenNoEntity_thenNothingDeleted() {
        var registry = new Registry();
        registry.deleteEntity(1);
        assertThat(registry.entities(new Query()))
            .isEmpty();
    }

    @Test
    void givenEntityDeleted_thenEntityIdReused() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.deleteEntity(entityId);
        assertThat(registry.createEntity()).isEqualTo(entityId);
    }

    @Test
    void givenEntityDeleted_thenEntityIsNotVisibleInComponentQuery() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value");
        registry.deleteEntity(entityId);
        assertThat(registry.entities(new Query().all(String.class)))
            .isEmpty();
    }

    @Test
    void givenEntityDeletedTwice_thenEntityIdReusedOnce() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.deleteEntity(entityId);
        registry.deleteEntity(entityId);
        assertThat(registry.createEntity()).isEqualTo(entityId);
        assertThat(registry.createEntity()).isNotEqualTo(entityId);
    }

    @Test
    void givenOneOfTwoEntitiesInSameTableDeleted_thenOtherEntityRemainsAccessible() {
        var registry = new Registry();
        var firstEntityId = registry.createEntity();
        var secondEntityId = registry.createEntity();
        registry.addComponents(firstEntityId, "first");
        registry.addComponents(secondEntityId, "second");
        registry.deleteEntity(firstEntityId);
        assertThat(registry.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(secondEntityId, "second");
    }

    @Test
    void givenMultipleEntitiesDeleted_thenEntityIdsReusedInOrder() {
        var registry = new Registry();
        var firstEntityId = registry.createEntity();
        var secondEntityId = registry.createEntity();
        registry.deleteEntity(firstEntityId);
        registry.deleteEntity(secondEntityId);
        assertThat(registry.createEntity()).isEqualTo(firstEntityId);
        assertThat(registry.createEntity()).isEqualTo(secondEntityId);
    }

    @Test
    void givenEntityExists_thenMultipleComponentsRemovedAtOnce() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value", 1, 1.0);
        registry.removeComponents(entityId, String.class, Integer.class);
        assertThat(registry.entities(new Query().all(Double.class).none(String.class, Integer.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(Double.class)
            )
            .containsExactly(entityId, 1.0);
    }

    @Test
    void givenEntityExists_thenComponentsAdded() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value");
        assertThat(registry.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "value");
    }

    @Test
    void givenNoEntity_thenAddComponentsThrowsException() {
        var registry = new Registry();
        assertThatNullPointerException()
            .isThrownBy(() -> registry.addComponents(1, ""));
    }

    @Test
    void givenEntityDeleted_thenAddComponentsThrowsException() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.deleteEntity(entityId);
        assertThatNullPointerException()
            .isThrownBy(() -> registry.addComponents(entityId, ""));
    }

    @Test
    void givenTwoEntitiesWithSameComponentTypes_thenComponentsUpdatedIndependently() {
        var registry = new Registry();
        var firstEntityId = registry.createEntity();
        var secondEntityId = registry.createEntity();
        registry.addComponents(firstEntityId, "first");
        registry.addComponents(secondEntityId, "second");
        registry.addComponents(firstEntityId, "updated");
        assertThat(registry.entities(new Query().all(String.class)))
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
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value", 1);
        assertThat(registry.entities(new Query().all(String.class, Integer.class)))
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
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "first");
        registry.addComponents(entityId, "second");
        assertThat(registry.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "second");
    }

    @Test
    void givenTwoEntities_thenEachHoldsItsOwnComponentValue() {
        var registry = new Registry();
        var firstEntityId = registry.createEntity();
        var secondEntityId = registry.createEntity();
        registry.addComponents(firstEntityId, "first");
        registry.addComponents(secondEntityId, "second");
        assertThat(registry.entities(new Query().all(String.class)))
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
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value", 1);
        registry.removeComponents(entityId, Integer.class);
        assertThat(registry.entities(new Query().all(String.class).none(Integer.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "value");
    }

    @Test
    void givenNoEntity_thenRemoveComponentsThrowsException() {
        var registry = new Registry();
        assertThatNullPointerException()
            .isThrownBy(() -> registry.removeComponents(1, String.class));
    }

    @Test
    void givenAllComponentsRemoved_thenEntityAppearsInEmptyQueryOnly() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value");
        registry.removeComponents(entityId, String.class);
        assertThat(registry.entities(new Query().all(String.class)))
            .isEmpty();
        assertThat(registry.entities(new Query()))
            .singleElement()
            .extracting(Entity::id)
            .isEqualTo(entityId);
    }

    @Test
    void givenComponentRemoved_thenOtherComponentsStillAccessible() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value", 1);
        registry.removeComponents(entityId, Integer.class);
        assertThat(registry.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "value");
    }

    @Test
    void givenRemovingNonExistentComponent_thenNothingRemoved() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.removeComponents(entityId, String.class);
        assertThat(registry.entities(new Query()))
            .singleElement()
            .extracting(Entity::id)
            .isEqualTo(entityId);
    }

    @Test
    void givenQueryWithAll_thenOnlyMatchingEntitiesReturned() {
        var registry = new Registry();
        var firstEntityId = registry.createEntity();
        var secondEntityId = registry.createEntity();
        registry.addComponents(firstEntityId, "value");
        registry.addComponents(secondEntityId, 1);
        assertThat(registry.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(firstEntityId, "value");
    }

    @Test
    void givenQueryWithAll_thenEntityWithSupersetOfComponentsIsReturned() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value", 1);
        assertThat(registry.entities(new Query().all(String.class)))
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
        var registry = new Registry();
        var firstEntityId = registry.createEntity();
        var secondEntityId = registry.createEntity();
        registry.addComponents(firstEntityId, "first");
        registry.addComponents(secondEntityId, "second", 1);
        assertThat(registry.entities(new Query().none(Integer.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(firstEntityId, "first");
    }

    @Test
    void givenQueryWithNone_thenEntityWithSupersetContainingExcludedTypeIsExcluded() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value", 1);
        assertThat(registry.entities(new Query().all(String.class).none(Integer.class)))
            .isEmpty();
    }

    @Test
    void givenQueryWithOne_thenEntityWithAtLeastOneMatchingComponentReturned() {
        var registry = new Registry();
        var firstEntityId = registry.createEntity();
        var secondEntityId = registry.createEntity();
        var thirdEntityId = registry.createEntity();
        registry.addComponents(firstEntityId, "value");
        registry.addComponents(secondEntityId, 1);
        registry.addComponents(thirdEntityId, new Object());
        assertThat(registry.entities(new Query().one(String.class, Integer.class)))
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
        var registry = new Registry();
        var firstEntityId = registry.createEntity();
        var secondEntityId = registry.createEntity();
        var thirdEntityId = registry.createEntity();
        registry.addComponents(firstEntityId, "first", 1);
        registry.addComponents(secondEntityId, "second", 1.0);
        registry.addComponents(thirdEntityId, "third", new Object());
        assertThat(
            registry.entities(
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
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value");
        assertThat(registry.entities(new Query().one(String.class)))
            .hasSameElementsAs(registry.entities(new Query().all(String.class)));
    }

    @Test
    void givenReusedEntityId_thenPreviousComponentsNotVisible() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value");
        registry.deleteEntity(entityId);
        registry.addComponents(registry.createEntity(), 1);
        assertThat(registry.entities(new Query().all(Integer.class).none(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(Integer.class)
            )
            .containsExactly(entityId, 1);
    }

    @Test
    void givenEntityMovedAcrossTables_thenQueryReflectsCurrentState() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value");
        assertThat(registry.entities(new Query().all(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class)
            )
            .containsExactly(entityId, "value");
        registry.addComponents(entityId, 1);
        assertThat(registry.entities(new Query().all(String.class, Integer.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(String.class),
                entity -> entity.component(Integer.class)
            )
            .containsExactly(entityId, "value", 1);
        registry.removeComponents(entityId, String.class);
        assertThat(registry.entities(new Query().all(Integer.class).none(String.class)))
            .singleElement()
            .extracting(
                Entity::id,
                entity -> entity.component(Integer.class)
            )
            .containsExactly(entityId, 1);
    }

    @Test
    void givenEntityWithComponent_thenAccessingAbsentComponentReturnsNull() {
        var registry = new Registry();
        var entityId = registry.createEntity();
        assertThat(registry.entities(new Query()))
            .singleElement()
            .extracting(Entity::id, entity -> entity.component(String.class))
            .containsExactly(entityId, null);
    }

    @Test
    void givenSameQuery_thenSameView() {
        var registry = new Registry();
        var first = registry.entities(new Query());
        var second = registry.entities(new Query());
        assertThat(first).isSameAs(second);
    }

    @Test
    void givenRegistryUpdatedAfterViewCreated_thenViewContainsNewEntities() {
        var registry = new Registry();
        var entities = registry.entities(new Query().all(String.class));
        assertThat(entities).isEmpty();
        var entityId = registry.createEntity();
        registry.addComponents(entityId, "value");
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
        var registry = new Registry();
        registry.addSystems(
            (_, deltaTimeSeconds) -> deltaTimeFromSystem.set(deltaTimeSeconds)
        );
        registry.update(12.34f);
        assertThat(deltaTimeFromSystem).hasValue(12.34f);
    }

    @Test
    void givenSystemAddedComponents_thenNextSystemSeeChanges() {
        var registry = new Registry();
        var entities = registry.entities(new Query().all(String.class));
        registry.addSystems(
            (registryEdit, _) -> registryEdit.addComponents(
                registryEdit.createEntity(),
                "value"
            ),
            (_, _) -> assertThat(entities)
                .singleElement()
                .extracting(entity -> entity.component(String.class))
                .isEqualTo("value")
        );
        registry.update(1);
    }

    @Test
    void givenSystemRemovedComponents_thenNextSystemSeeChanges() {
        var registry = new Registry();
        registry.addComponents(registry.createEntity(), "value");
        var entities = registry.entities(new Query().all(String.class));
        registry.addSystems(
            (registryEdit, _) -> {
                for (var entity : entities) {
                    registryEdit.removeComponents(entity.id(), String.class);
                }
                assertThat(entities)
                    .singleElement()
                    .extracting(entity -> entity.component(String.class))
                    .isEqualTo("value");
            },
            (_, _) -> assertThat(entities).isEmpty()
        );
        registry.update(1);
    }

    @Test
    void givenSystemDeletedEntity_thenNextSystemSeeChanges() {
        var registry = new Registry();
        registry.addComponents(registry.createEntity(), "value");
        var entities = registry.entities(new Query());
        registry.addSystems(
            (registryEdit, _) -> {
                for (var entity : entities) {
                    registryEdit.deleteEntity(entity.id());
                }
                assertThat(entities)
                    .singleElement()
                    .extracting(entity -> entity.component(String.class))
                    .isEqualTo("value");
            },
            (_, _) -> assertThat(entities).isEmpty()
        );
        registry.update(1);
    }
}