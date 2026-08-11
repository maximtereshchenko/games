package com.github.maximtereshchenko.games.ecs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class DenseIntArrayTest {

    @Test
    void givenEmptyArray_thenSizeZero() {
        assertThat(new DenseIntArray().size()).isZero();
    }

    @Test
    void givenRemoveOnEmptyArray_thenSizeZero() {
        var denseArray = new DenseIntArray();
        denseArray.remove(0);
        assertThat(denseArray.size()).isZero();
    }

    @Test
    void givenElementSet_thenSizeOne() {
        var denseArray = new DenseIntArray();
        denseArray.set(0, 0);
        assertThat(denseArray.size()).isOne();
    }

    @Test
    void givenElementOverridden_thenSizeOne() {
        var denseArray = new DenseIntArray();
        denseArray.set(0, 1);
        denseArray.set(0, 2);
        assertThat(denseArray.size()).isOne();
    }

    @Test
    void givenElementRemoved_thenSizeZero() {
        var denseArray = new DenseIntArray();
        denseArray.set(0, 0);
        denseArray.remove(0);
        assertThat(denseArray.size()).isZero();
    }

    @Test
    void givenEmptyArray_thenNullElement() {
        assertThat(new DenseIntArray().element(0))
            .isEqualTo(DenseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenElementSet_thenElement() {
        var denseArray = new DenseIntArray();
        denseArray.set(0, 1);
        assertThat(denseArray.element(0)).isOne();
    }

    @Test
    void givenRequiredIndexGreaterThanSize_thenNullElement() {
        assertThat(new DenseIntArray(1).element(1))
            .isEqualTo(DenseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenRequiredIndexGreaterThanSize_thenElementSet() {
        var denseArray = new DenseIntArray(1);
        denseArray.set(1, 1);
        assertThat(denseArray.element(1)).isOne();
    }

    @Test
    void givenLastElementRemoved_thenElementRemoved() {
        var denseArray = new DenseIntArray();
        denseArray.set(0, 1);
        denseArray.set(1, 2);
        assertThat(denseArray.remove(1)).isEqualTo(DenseIntArray.EMPTY_VALUE);
        assertThat(denseArray.element(0)).isEqualTo(1);
        assertThat(denseArray.element(1)).isEqualTo(DenseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenNonLastElementRemoved_thenElementReplacedWithLast() {
        var denseArray = new DenseIntArray();
        denseArray.set(0, 1);
        denseArray.set(1, 2);
        assertThat(denseArray.remove(0)).isEqualTo(2);
        assertThat(denseArray.element(0)).isEqualTo(2);
        assertThat(denseArray.element(1)).isEqualTo(DenseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenLastElementRemoved_thenEmptyValueAtThatIndex() {
        var denseArray = new DenseIntArray();
        denseArray.set(0, 1);
        denseArray.set(1, 2);
        denseArray.remove(0);
        denseArray.set(2, 3);
        assertThat(denseArray.element(1)).isEqualTo(DenseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenGap_thenEmptyValueAtGapIndex() {
        var denseArray = new DenseIntArray(1);
        denseArray.set(2, 1);
        assertThat(denseArray.element(1)).isEqualTo(DenseIntArray.EMPTY_VALUE);
    }
}