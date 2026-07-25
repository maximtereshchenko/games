package com.github.maximtereshchenko.ecs;

import java.util.Arrays;

final class SparseArray<T> {

    private final int pageShift;
    private final int pageMask;
    private T[][] pages;

    @SuppressWarnings("unchecked")
    SparseArray(int pageShift, int initialPages) {
        this.pageShift = pageShift;
        this.pageMask = (1 << pageShift) - 1;
        this.pages = (T[][]) new Object[initialPages][];
    }

    SparseArray() {
        this(10, 16);
    }

    @SuppressWarnings("unchecked")
    void set(int index, T element) {
        var pageIndex = pageIndex(index);
        while (pageIndex >= pages.length) {
            pages = Arrays.copyOf(pages, pages.length << 1);
        }
        var page = pages[pageIndex];
        if (page == null) {
            page = (T[]) new Object[1 << pageShift];
            pages[pageIndex] = page;
        }
        page[elementIndex(index)] = element;
    }

    T element(int index) {
        var pageIndex = pageIndex(index);
        if (pageIndex >= pages.length) {
            return null;
        }
        var page = pages[pageIndex];
        if (page == null) {
            return null;
        }
        return page[elementIndex(index)];
    }

    void remove(int index) {
        var pageIndex = pageIndex(index);
        if (pageIndex >= pages.length) {
            return;
        }
        var page = pages[pageIndex];
        if (page == null) {
            return;
        }
        page[elementIndex(index)] = null;
    }

    private int pageIndex(int index) {
        return index >> pageShift;
    }

    private int elementIndex(int index) {
        return index & pageMask;
    }
}
