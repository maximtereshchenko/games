package com.github.maximtereshchenko.ecs;

import java.util.Arrays;

final class SparseIntArray {

    static final int EMPTY_VALUE = -1;

    private final int pageShift;
    private final int pageMask;
    private int[][] pages;

    SparseIntArray(int pageShift, int initialPages) {
        this.pageShift = pageShift;
        this.pageMask = (1 << pageShift) - 1;
        this.pages = new int[initialPages][];
    }

    SparseIntArray() {
        this(10, 16);
    }

    void set(int index, int element) {
        var pageIndex = pageIndex(index);
        while (pageIndex >= pages.length) {
            pages = Arrays.copyOf(pages, pages.length << 1);
        }
        var page = pages[pageIndex];
        if (page == null) {
            page = new int[1 << pageShift];
            Arrays.fill(page, EMPTY_VALUE);
            pages[pageIndex] = page;
        }
        page[elementIndex(index)] = element;
    }

    int element(int index) {
        var pageIndex = pageIndex(index);
        if (pageIndex >= pages.length) {
            return EMPTY_VALUE;
        }
        var page = pages[pageIndex];
        if (page == null) {
            return EMPTY_VALUE;
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
        page[elementIndex(index)] = EMPTY_VALUE;
    }

    private int pageIndex(int index) {
        return index >> pageShift;
    }

    private int elementIndex(int index) {
        return index & pageMask;
    }
}
