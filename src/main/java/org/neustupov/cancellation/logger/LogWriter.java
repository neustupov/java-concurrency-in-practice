package org.neustupov.cancellation.logger;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogWriter {

    private final static Integer CAPACITY = 100;

    private final BlockingQueue<String> queue;
    private final LoggerThread logger;

    public LogWriter(Writer writer) {
        this.queue = new LinkedBlockingQueue<>(CAPACITY);
        this.logger = new LoggerThread(writer);
    }

    public void start() {
        logger.start();
    }

    public void log(String msg) throws InterruptedException {
        queue.put(msg);
    }

    class LoggerThread extends Thread {

        private Writer writer;

        public LoggerThread(Writer writer) {
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    writer.append(queue.take());
                }
            } catch (InterruptedException e) {

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
