package com.fschoen.parlorplace.backend.utility.other;

import java.util.HashMap;

public class MapBuilder<K, V> {

    private final HashMap<K,V> map = new HashMap<>();

    public MapBuilder<K,V> put(K k, V v) {
        this.map.put(k, v);
        return this;
    }

    public HashMap<K, V> build() {
        return this.map;
    }

}
