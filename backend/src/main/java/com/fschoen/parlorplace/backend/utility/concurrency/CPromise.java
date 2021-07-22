package com.fschoen.parlorplace.backend.utility.concurrency;

import java.util.Collection;

public class CPromise<V extends Collection<C>, C> extends Promise<V> {

    public CPromise(int needed) {
        super(needed);
    }

    public void add(C c) {
        synchronized (this.valueLock) {
            this.value.add(c);
        }
    }

}
