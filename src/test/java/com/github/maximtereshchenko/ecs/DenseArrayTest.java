package com.github.maximtereshchenko.ecs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class DenseArrayTest {

    @Test
    void givenEmptyArray_thenSizeZero() {
        assertThat(new DenseArray().size()).isZero();
    }

    @Test
    void givenRemoveOnEmptyArray_thenSizeZero() {
        var denseArray = new DenseArray();
        denseArray.remove(0);
        assertThat(denseArray.size()).isZero();
    }

    @Test
    void givenElementSet_thenSizeOne() {
        var denseArray = new DenseArray();
        denseArray.set(0, new Object());
        assertThat(denseArray.size()).isOne();
    }

    @Test
    void givenElementOverridden_thenSizeOne() {
        var denseArray = new DenseArray();
        denseArray.set(0, "first");
        denseArray.set(0, "second");
        assertThat(denseArray.size()).isOne();
    }

    @Test
    void givenElementRemoved_thenSizeZero() {
        var denseArray = new DenseArray();
        denseArray.set(0, new Object());
        denseArray.remove(0);
        assertThat(denseArray.size()).isZero();
    }

    @Test
    void givenEmptyArray_thenNullElement() {
        assertThat(new DenseArray().element(0)).isNull();
    }

    @Test
    void givenElementSet_thenElement() {
        var denseArray = new DenseArray();
        denseArray.set(0, "value");
        assertThat(denseArray.element(0)).isEqualTo("value");
    }

    @Test
    void givenRequiredIndexGreaterThanSize_thenNullElement() {
        assertThat(new DenseArray(1).element(1)).isNull();
    }

    @Test
    void givenRequiredIndexGreaterThanSize_thenElementSet() {
        var denseArray = new DenseArray(1);
        denseArray.set(1, "value");
        assertThat(denseArray.element(1)).isEqualTo("value");
    }

    @Test
    void givenLastElementRemoved_thenElementRemoved() {
        var denseArray = new DenseArray();
        denseArray.set(0, "first");
        denseArray.set(1, "second");
        assertThat(denseArray.remove(1)).isNull();
        assertThat(denseArray.element(0)).isEqualTo("first");
        assertThat(denseArray.element(1)).isNull();
    }

    @Test
    void givenNonLastElementRemoved_thenElementReplacedWithLast() {
        var denseArray = new DenseArray();
        denseArray.set(0, "first");
        denseArray.set(1, "second");
        assertThat(denseArray.remove(0)).isEqualTo("second");
        assertThat(denseArray.element(0)).isEqualTo("second");
        assertThat(denseArray.element(1)).isNull();
    }

    @Test
    void givenLastElementRemoved_thenEmptyValueAtThatIndex() {
        var denseArray = new DenseArray();
        denseArray.set(0, 1);
        denseArray.set(1, 2);
        denseArray.remove(0);
        denseArray.set(2, 3);
        assertThat(denseArray.element(1)).isNull();
    }
}