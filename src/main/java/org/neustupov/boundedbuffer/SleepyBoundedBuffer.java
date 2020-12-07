package org.neustupov.boundedbuffer;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer {

    private static final int SLEEP_GRANULARITY = 100;

    public SleepyBoundedBuffer(int capacity) {
        super(capacity);
    }

    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }

    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty()) {
                    return (V) doTake();
                }
            }

            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
}
