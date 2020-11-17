package org.neustupov.pools;

public interface ThreadFactory {
    Thread newThread(Runnable r);
}
