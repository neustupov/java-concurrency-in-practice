package org.neustupov.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

abstract class BackgroundTask<V> implements Runnable, Future<V> {

    private JButton startButton = new JButton();
    private JButton cancelButton = new JButton();

    private final FutureTask<V> computation = new Computation();

    private class Computation extends FutureTask<V> {
        public Computation() {
            super(BackgroundTask.this::compute);
        }
    }

    protected final void done() {
        GuiExecutor.getInstance().execute(() -> {
            V value = null;
            Throwable thrown = null;
            boolean canceled = false;
            try {
                value = get();
            } catch (ExecutionException e) {
                thrown = e.getCause();
            } catch (CancellationException e) {
                canceled = true;
            } catch (InterruptedException e) {

            } finally {
                onCompletion(value, thrown, canceled);
            }
        });
    }

    protected void setProgress(final int current, final int max) {
        GuiExecutor.getInstance().execute(() -> {
            onProgress(current, max);
        });
    }

    protected abstract V compute();

    protected void onCompletion(V result, Throwable exception, boolean canceled) {
    }

    protected void onProgress(int current, int max) {
    }

    public void runInBackground(final Runnable task){
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                class CancelListener implements ActionListener{
                    BackgroundTask<?> task;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(task != null){
                            task.cancel(true);
                        }
                    }
                }
                final CancelListener listener = new CancelListener();
                listener.task = new BackgroundTask<Void>() {
                    @Override
                    protected Void compute() {
                        while (!isCancelled()){
                            //doSomeWork();
                        }
                        return null;
                    }

                    public void onCompletion(boolean canceled, String s, Throwable exception){
                        cancelButton.removeActionListener(listener);
                    }

                    @Override
                    public void run() {

                    }

                    @Override
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public boolean isDone() {
                        return false;
                    }

                    @Override
                    public Void get() throws InterruptedException, ExecutionException {
                        return null;
                    }

                    @Override
                    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null;
                    }
                };
                cancelButton.addActionListener(listener);
            }
        });
    }
}
