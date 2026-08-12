package com.github.maximtereshchenko.games.ecs;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class Registry implements RegistryEdit {

    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private final NewEntityIds newEntityIds;
    private final ComponentTypeIdRegistry componentTypeIdRegistry;
    private final Map<BitSet, Table> tables;
    private final SparseArray<Table> tablesByEntityId;
    private final Table root;
    private final Map<ViewMask, View> views;
    private final List<System> systems;
    private final BatchingRegistryEdit batchingRegistryEdit;
    private final BitSet bitSet;

    public Registry() {
        this.newEntityIds = new NewEntityIds();
        this.componentTypeIdRegistry = new ComponentTypeIdRegistry();
        this.tables = new HashMap<>();
        this.tablesByEntityId = new SparseArray<>();
        this.root = new Table(new BitSet());
        this.views = new HashMap<>();
        this.systems = new ArrayList<>();
        this.batchingRegistryEdit = new BatchingRegistryEdit(this, newEntityIds);
        this.bitSet = new BitSet();
        tables.put(root.mask(), root);
    }

    @Override
    public int createEntity() {
        var id = newEntityIds.next();
        createEntity(id);
        return id;
    }

    @Override
    public void deleteEntity(int id) {
        var table = tablesByEntityId.element(id);
        if (table == null) {
            return;
        }
        table.delete(id);
        tablesByEntityId.remove(id);
        newEntityIds.free(id);
    }

    @Override
    public void addComponents(int id, Object... components) {
        var componentTypeIds = new int[components.length];
        updateComponents(
            id,
            mask -> {
                for (var i = 0; i < components.length; i++) {
                    var componentTypeId = componentTypeIdRegistry.get(components[i].getClass());
                    componentTypeIds[i] = componentTypeId;
                    mask.set(componentTypeId);
                }
            },
            source -> source.update(id, componentTypeIds, components),
            (source, target) -> target.transfer(source, id, componentTypeIds, components)
        );
    }

    @Override
    public void removeComponents(int id, Class<?>... types) {
        updateComponents(
            id,
            mask -> {
                for (var type : types) {
                    mask.clear(componentTypeIdRegistry.get(type));
                }
            },
            _ -> {},
            (source, target) -> target.transfer(source, id, EMPTY_INT_ARRAY, EMPTY_OBJECT_ARRAY)
        );
    }

    public Iterable<Entity> entities(Query query) {
        return views.computeIfAbsent(
            query.viewMask(componentTypeIdRegistry),
            this::newView
        );
    }

    public void addSystems(System... systems) {
        this.systems.addAll(Arrays.asList(systems));
    }

    public void update(float deltaTimeSeconds) {
        for (var system : systems) {
            system.update(batchingRegistryEdit, deltaTimeSeconds);
            batchingRegistryEdit.flush();
        }
    }

    void createEntity(int id) {
        root.insert(id);
        tablesByEntityId.set(id, root);
    }

    private void updateComponents(
        int id,
        Consumer<BitSet> maskInitializer,
        Consumer<Table> update,
        BiConsumer<Table, Table> transfer
    ) {
        var source = Objects.requireNonNull(tablesByEntityId.element(id));
        bitSet.clear();
        bitSet.or(source.mask());
        maskInitializer.accept(bitSet);
        if (bitSet.equals(source.mask())) {
            update.accept(source);
            return;
        }
        var target = table();
        transfer.accept(source, target);
        tablesByEntityId.set(id, target);
    }

    private Table table() {
        var existing = tables.get(bitSet);
        if (existing != null) {
            return existing;
        }
        var table = new Table((BitSet) bitSet.clone());
        for (var view : views.values()) {
            view.addTable(table);
        }
        tables.put(table.mask(), table);
        return table;
    }

    private View newView(ViewMask queryMask) {
        var view = new View(
            queryMask,
            componentTypeIdRegistry
        );
        for (var table : tables.values()) {
            view.addTable(table);
        }
        return view;
    }
}
