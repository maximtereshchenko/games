package com.github.maximtereshchenko.games.ecs;

import java.util.ArrayList;
import java.util.List;

final class BatchingRegistryEdit implements RegistryEdit {

    private final Registry registry;
    private final NewEntityIds newEntityIds;
    private final List<Modification> modifications;

    BatchingRegistryEdit(Registry registry, NewEntityIds newEntityIds) {
        this.registry = registry;
        this.newEntityIds = newEntityIds;
        this.modifications = new ArrayList<>();
    }

    @Override
    public int createEntity() {
        var id = newEntityIds.next();
        modifications.add(new CreateEntity(id));
        return id;
    }

    @Override
    public void deleteEntity(int id) {
        modifications.add(new DeleteEntity(id));
    }

    @Override
    public void addComponents(int id, Object... components) {
        modifications.add(new AddComponents(id, components));
    }

    @Override
    public void removeComponents(int id, Class<?>... types) {
        modifications.add(new RemoveComponents(id, types));
    }

    void flush() {
        for (var modification : modifications) {
            switch (modification) {
                case CreateEntity createEntity -> registry.createEntity(createEntity.id());
                case DeleteEntity deleteEntity -> registry.deleteEntity(deleteEntity.id());
                case AddComponents addComponents -> registry.addComponents(
                    addComponents.id(),
                    addComponents.components()
                );
                case RemoveComponents removeComponents -> registry.removeComponents(
                    removeComponents.id(),
                    removeComponents.types()
                );
            }
        }
        modifications.clear();
    }

    private sealed interface Modification {}

    private record CreateEntity(int id) implements Modification {}

    private record DeleteEntity(int id) implements Modification {}

    private record AddComponents(int id, Object[] components) implements Modification {}

    private record RemoveComponents(int id, Class<?>[] types) implements Modification {}
}
