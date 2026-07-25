package com.github.maximtereshchenko.ecs;

import java.util.ArrayList;
import java.util.List;

final class BatchingWorldEdit implements WorldEdit {

    private final World world;
    private final NewEntityIds newEntityIds;
    private final List<Modification> modifications;

    BatchingWorldEdit(World world, NewEntityIds newEntityIds) {
        this.world = world;
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
                case CreateEntity createEntity -> world.createEntity(createEntity.id());
                case DeleteEntity deleteEntity -> world.deleteEntity(deleteEntity.id());
                case AddComponents addComponents -> world.addComponents(
                    addComponents.id(),
                    addComponents.components()
                );
                case RemoveComponents removeComponents -> world.removeComponents(
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
