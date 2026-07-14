package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.*;

final class SameThreadDominion implements Dominion {

    private final Dominion original;

    SameThreadDominion(Dominion original) {
        this.original = original;
    }

    @Override
    public String getName() {
        return original.getName();
    }

    @Override
    public Entity createEntity(Object... components) {
        return original.createEntity(components);
    }

    @Override
    public Entity createPreparedEntity(Composition.OfTypes withValues) {
        return original.createPreparedEntity(withValues);
    }

    @Override
    public Entity createEntityAs(Entity prefab, Object... components) {
        return original.createEntityAs(prefab, components);
    }

    @Override
    public boolean deleteEntity(Entity entity) {
        return original.deleteEntity(entity);
    }

    @Override
    public boolean modifyEntity(Composition.Modifier modifier) {
        return original.modifyEntity(modifier);
    }

    @Override
    public Composition composition() {
        return original.composition();
    }

    @Override
    public Scheduler createScheduler() {
        return new SameThreadScheduler();
    }

    @Override
    public <T> Results<T> findCompositionsWith(Class<T> type) {
        return original.findCompositionsWith(type);
    }

    @Override
    public <T1, T2> Results<Results.With2<T1, T2>> findCompositionsWith(
        Class<T1> type1,
        Class<T2> type2
    ) {
        return original.findCompositionsWith(type1, type2);
    }

    @Override
    public <T1, T2, T3> Results<Results.With3<T1, T2, T3>> findCompositionsWith(
        Class<T1> type1,
        Class<T2> type2,
        Class<T3> type3
    ) {
        return original.findCompositionsWith(type1, type2, type3);
    }

    @Override
    public <T1, T2, T3, T4> Results<Results.With4<T1, T2, T3, T4>> findCompositionsWith(
        Class<T1> type1,
        Class<T2> type2,
        Class<T3> type3,
        Class<T4> type4
    ) {
        return original.findCompositionsWith(
            type1,
            type2,
            type3,
            type4
        );
    }

    @Override
    public <T1, T2, T3, T4, T5> Results<Results.With5<T1, T2, T3, T4, T5>> findCompositionsWith(
        Class<T1> type1,
        Class<T2> type2,
        Class<T3> type3,
        Class<T4> type4,
        Class<T5> type5
    ) {
        return original.findCompositionsWith(type1, type2, type3, type4, type5);
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> Results<Results.With6<T1, T2, T3, T4, T5, T6>> findCompositionsWith(
        Class<T1> type1,
        Class<T2> type2,
        Class<T3> type3,
        Class<T4> type4,
        Class<T5> type5,
        Class<T6> type6
    ) {
        return original.findCompositionsWith(type1, type2, type3, type4, type5, type6);
    }

    @Override
    public <T extends Entity> Results<T> findAllEntities() {
        return original.findAllEntities();
    }

    @Override
    public <T> Results<Results.With1<T>> findEntitiesWith(Class<T> type) {
        return original.findEntitiesWith(type);
    }

    @Override
    public <T1, T2> Results<Results.With2<T1, T2>> findEntitiesWith(
        Class<T1> type1,
        Class<T2> type2
    ) {
        return original.findEntitiesWith(type1, type2);
    }

    @Override
    public <T1, T2, T3> Results<Results.With3<T1, T2, T3>> findEntitiesWith(
        Class<T1> type1,
        Class<T2> type2,
        Class<T3> type3
    ) {
        return original.findEntitiesWith(type1, type2, type3);
    }

    @Override
    public <T1, T2, T3, T4> Results<Results.With4<T1, T2, T3, T4>> findEntitiesWith(
        Class<T1> type1,
        Class<T2> type2,
        Class<T3> type3,
        Class<T4> type4
    ) {
        return original.findEntitiesWith(
            type1,
            type2,
            type3,
            type4
        );
    }

    @Override
    public <T1, T2, T3, T4, T5> Results<Results.With5<T1, T2, T3, T4, T5>> findEntitiesWith(
        Class<T1> type1,
        Class<T2> type2,
        Class<T3> type3,
        Class<T4> type4,
        Class<T5> type5
    ) {
        return original.findEntitiesWith(type1, type2, type3, type4, type5);
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> Results<Results.With6<T1, T2, T3, T4, T5, T6>> findEntitiesWith(
        Class<T1> type1,
        Class<T2> type2,
        Class<T3> type3,
        Class<T4> type4,
        Class<T5> type5,
        Class<T6> type6
    ) {
        return original.findEntitiesWith(type1, type2, type3, type4, type5, type6);
    }

    @Override
    public boolean isClosed() {
        return original.isClosed();
    }

    @Override
    public void close() {
        original.close();
    }
}
