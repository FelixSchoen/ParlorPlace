package com.fschoen.parlorplace.backend.utility.other;

import java.util.ArrayList;
import java.util.List;

public class ListBuilder<T> {

    private final ArrayList<T> list = new ArrayList<>();

    public ListBuilder<T> add(T value) {
        this.list.add(value);
        return this;
    }

    public List<T> build() {
        return this.list;
    }

}
