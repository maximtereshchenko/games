package com.github.maximtereshchenko.ecs;

public interface WorldEdit {

    int createEntity();

    void deleteEntity(int id);

    void addComponents(int id, Object... components);

    void removeComponents(int id, Class<?>... types);
}
