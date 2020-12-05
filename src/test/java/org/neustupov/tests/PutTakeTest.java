package org.neustupov.tests;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutTakeTest {
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private final AtomicInteger putSum = new AtomicInteger(0);
    private final AtomicInteger takeSum = new AtomicInteger(0);
    private final BarrierTimer timer;
    private final CyclicBarrier barrier;
    private final BoundedBuffer<Integer> bb;
    private final int nTrials, nPairs;

    @Test
    public static void main(String[] args) throws Exception {
        int tpt = 100000;
        for(int cap = 1; cap <= 1000; cap *= 10){
            System.out.println("Capacity " + cap);
            for(int pairs = 1; pairs <= 128; pairs *= 2){
                PutTakeTest t = new PutTakeTest(cap, pairs, tpt);
                System.out.println("Pairs: " + pairs);
                t.test();
                System.out.println("\t");
                Thread.sleep(1000);
                t.test();
                System.out.println();
                Thread.sleep(1000);
            }
        }
        new PutTakeTest(10, 10, 100000).test();
        new PutTakeTest(10, 10, 10).testPoolExpasion();
        pool.shutdown();
    }

    @Test
    public void testPoolExpasion() throws InterruptedException {
        int MAX_SIZE = 10;
        TestingThreadFactory testingThreadFactory = new TestingThreadFactory();
        ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE, testingThreadFactory);

        for (int i = 0; i < 10 * MAX_SIZE; i++) {
            exec.execute(() -> {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (int i = 0; i < 20 && testingThreadFactory.numCreated.get() < MAX_SIZE; i++) {
            Thread.sleep(100);
        }

        assertEquals(testingThreadFactory.numCreated.get(), MAX_SIZE);
        exec.shutdown();
    }

    PutTakeTest(int capacity, int npairs, int ntials) {
        this.bb = new BoundedBuffer<>(capacity);
        this.nTrials = ntials;
        this.nPairs = npairs;
        this.timer = new BarrierTimer();
        this.barrier = new CyclicBarrier(npairs * 2 + 1, timer);
    }

    void test() {
        try {
            timer.clear();
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await();
            barrier.await();
            long nsPerItem = timer.getTime() / (nPairs * nTrials);
            System.out.println(nsPerItem);
            assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                int seed = (this.hashCode() ^ (int) System.nanoTime());
                int sum = 0;
                barrier.await();
                for (int i = nTrials; i > 0; --i) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Consumer implements Runnable {
        public void run() {
            try {
                barrier.await();
                int sum = 0;
                for (int i = nTrials; i > 0; --i) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    public class BarrierTimer implements Runnable {
        private boolean started;
        private long startTime, endTime;

        public synchronized void run() {
            long t = System.nanoTime();
            if (!started) {
                started = true;
                startTime = t;
            } else {
                endTime = t;
            }
        }

        public synchronized void clear() {
            started = false;
        }

        public synchronized long getTime() {
            return endTime - startTime;
        }
    }
}
