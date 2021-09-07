package com.fschoen.parlorplace.backend.utility.other;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SetBuilder<T> {

    private final HashSet<T> set = new HashSet<>();

    public SetBuilder<T> add(T value) {
        this.set.add(value);
        return this;
    }

    public SetBuilder<T> addAll(Collection<T> value) {
        this.set.addAll(value);
        return this;
    }

    public Set<T> build() {
        return this.set;
    }

}
