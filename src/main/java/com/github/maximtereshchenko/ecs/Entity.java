package com.github.maximtereshchenko.ecs;

import java.util.Objects;

public final class Entity {

    private final int id;
    private final ComponentTypeIdRegistry componentTypeIdRegistry;
    private final Table table;

    Entity(int id, ComponentTypeIdRegistry componentTypeIdRegistry, Table table) {
        this.id = id;
        this.componentTypeIdRegistry = componentTypeIdRegistry;
        this.table = table;
    }

    public int id() {
        return id;
    }

    public <T> T component(Class<T> type) {
        return type.cast(table.component(id, componentTypeIdRegistry.get(type)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof Entity entity &&
               id == entity.id;
    }
}
