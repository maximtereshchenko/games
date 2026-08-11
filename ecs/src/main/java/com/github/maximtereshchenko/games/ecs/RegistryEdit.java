package com.github.maximtereshchenko.games.ecs;

public interface RegistryEdit {

    int createEntity();

    void deleteEntity(int id);

    void addComponents(int id, Object... components);

    void removeComponents(int id, Class<?>... types);
}
