package com.fschoen.parlorplace.backend.utility.concurrency;

import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

@Slf4j
public class Promise<V> {

    protected final Semaphore semaphore;

    protected V value;
    protected final Object valueLock;

    public Promise(int needed) {
        this.semaphore = new Semaphore(-needed + 1);
        this.valueLock = new Object();
    }

    public void write(V value) {
        synchronized (valueLock) {
            this.value = value;
        }
    }

    public V get() {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            log.error(Messages.getExceptionExplanationMessage("multithreading.promise.interrupted"));
        }
        return this.value;
    }

    public void release() {
        this.semaphore.release();
    }

}
