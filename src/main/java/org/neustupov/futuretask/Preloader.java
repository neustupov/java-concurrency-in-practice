package org.neustupov.futuretask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Preloader {

    private final FutureTask<ProductInfo> future = new FutureTask<>(ProductInfo::new);

    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public ProductInfo get() throws InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            System.out.println("Exception");
        }
        return null;
    }
}
