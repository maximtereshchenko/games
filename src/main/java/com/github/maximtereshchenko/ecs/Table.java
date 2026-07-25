package com.github.maximtereshchenko.ecs;

import java.util.BitSet;

final class Table {

    private final BitSet mask;
    private final DenseIntArray entityIds;
    private final SparseIntArray rowsByEntityId;
    private final SparseIntArray columnIndicesByComponentTypeId;
    private final DenseArray[] columns;

    Table(BitSet mask) {
        this.mask = mask;
        this.entityIds = new DenseIntArray();
        this.rowsByEntityId = new SparseIntArray();
        this.columnIndicesByComponentTypeId = new SparseIntArray();
        this.columns = new DenseArray[mask.cardinality()];
        for (
            int componentTypeId = mask.nextSetBit(0), index = 0;
            componentTypeId != -1;
            componentTypeId = mask.nextSetBit(componentTypeId + 1), index++
        ) {
            columns[index] = new DenseArray();
            columnIndicesByComponentTypeId.set(componentTypeId, index);
        }
    }

    BitSet mask() {
        return mask;
    }

    DenseIntArray entityIds() {
        return entityIds;
    }

    void insert(int entityId) {
        var row = entityIds.size();
        for (var column : columns) {
            column.set(row, null);
        }
        entityIds.set(row, entityId);
        rowsByEntityId.set(entityId, row);
    }

    void update(int entityId, int[] componentTypeIds, Object[] components) {
        updateRow(rowsByEntityId.element(entityId), componentTypeIds, components);
    }

    void delete(int entityId) {
        var row = rowsByEntityId.element(entityId);
        for (var column : columns) {
            column.remove(row);
        }
        var replacementEntityId = entityIds.remove(row);
        if (replacementEntityId != DenseIntArray.EMPTY_VALUE) {
            rowsByEntityId.set(replacementEntityId, row);
        }
        rowsByEntityId.remove(entityId);
    }

    Object component(int entityId, int componentTypeId) {
        var columnIndex = columnIndicesByComponentTypeId.element(componentTypeId);
        if (columnIndex == SparseIntArray.EMPTY_VALUE) {
            throw new IllegalArgumentException();
        }
        return columns[columnIndex]
            .element(rowsByEntityId.element(entityId));
    }

    void transfer(
        Table source,
        int entityId,
        int[] extraComponentTypeIds,
        Object[] extraComponents
    ) {
        var targetRow = entityIds.size();
        var sourceRow = source.rowsByEntityId.element(entityId);
        for (
            var componentTypeId = mask.nextSetBit(0);
            componentTypeId != -1;
            componentTypeId = mask.nextSetBit(componentTypeId + 1)
        ) {
            copy(source, componentTypeId, sourceRow, targetRow);
        }
        updateRow(targetRow, extraComponentTypeIds, extraComponents);
        source.delete(entityId);
        entityIds.set(targetRow, entityId);
        rowsByEntityId.set(entityId, targetRow);
    }

    private void copy(Table source, int componentTypeId, int sourceRow, int targetRow) {
        var sourceColumnIndex = source.columnIndicesByComponentTypeId.element(componentTypeId);
        if (sourceColumnIndex == SparseIntArray.EMPTY_VALUE) {
            return;
        }
        columns[columnIndicesByComponentTypeId.element(componentTypeId)]
            .set(
                targetRow,
                source.columns[sourceColumnIndex].element(sourceRow)
            );
    }

    private void updateRow(int row, int[] componentTypeIds, Object[] components) {
        for (var i = 0; i < componentTypeIds.length; i++) {
            columns[columnIndicesByComponentTypeId.element(componentTypeIds[i])]
                .set(row, components[i]);
        }
    }
}
