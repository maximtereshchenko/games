package com.github.maximtereshchenko.games.ecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class View implements Iterable<Entity> {

    private final ViewMask queryMask;
    private final ComponentTypeIdRegistry componentTypeIdRegistry;
    private final List<Table> tables;

    View(
        ViewMask queryMask,
        ComponentTypeIdRegistry componentTypeIdRegistry
    ) {
        this.queryMask = queryMask;
        this.componentTypeIdRegistry = componentTypeIdRegistry;
        this.tables = new ArrayList<>();
    }

    @Override
    public Iterator<Entity> iterator() {
        return new EntityIterator();
    }

    public int size() {
        return tables.stream()
            .mapToInt(Table::size)
            .sum();
    }

    void addTable(Table table) {
        if (queryMask.matches(table.mask())) {
            tables.add(table);
        }
    }

    private final class EntityIterator implements Iterator<Entity> {

        private int tableIndex = 0;
        private Table table;
        private int row;

        @Override
        public boolean hasNext() {
            while (tableIndex < tables.size() && table == null) {
                var next = tables.get(tableIndex++);
                if (next.entityIds().size() != 0) {
                    table = next;
                    row = 0;
                }
            }
            return table != null;
        }

        @Override
        public Entity next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            var entityIds = table.entityIds();
            var entity = new Entity(
                entityIds.element(row++),
                componentTypeIdRegistry,
                table
            );
            if (row == entityIds.size()) {
                table = null;
            }
            return entity;
        }
    }
}
