package org.neustupov;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

@NotThreadSafe
public class MutableInteger {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @ThreadSafe
    static class SynchronizedInteger {

        @GuardedBy("this")
        private int value;

        public synchronized int getValue() {
            return value;
        }

        public synchronized void setValue(int value) {
            this.value = value;
        }
    }
}
