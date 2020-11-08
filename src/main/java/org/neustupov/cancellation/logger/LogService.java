package org.neustupov.cancellation.logger;

import net.jcip.annotations.GuardedBy;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class LogService {

    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter printWriter;

    @GuardedBy("this")
    private boolean isShutdown;
    @GuardedBy("this")
    private int reservation;

    public LogService(BlockingQueue<String> queue, LoggerThread loggerThread, PrintWriter printWriter) {
        this.queue = queue;
        this.loggerThread = loggerThread;
        this.printWriter = printWriter;
    }

    public void start() {
        loggerThread.start();
    }

    public void stop() {
        synchronized (this) {
            isShutdown = true;
            loggerThread.interrupt();
        }
    }

    public void log(String msg) throws InterruptedException {
        synchronized (this) {
            if (isShutdown) {
                throw new IllegalStateException();
            }
            ++reservation;
        }
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        public void run() {
            try {
                while (true) {
                    try {
                        synchronized (this) {
                            if (isShutdown && reservation == 0) {
                                break;
                            }
                        }
                        String msg = queue.take();
                        synchronized (this) {
                            --reservation;
                        }
                        printWriter.write(msg);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupt");
                    }
                }
            } finally {
                printWriter.close();
            }
        }
    }
}
