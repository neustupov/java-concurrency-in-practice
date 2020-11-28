package org.neustupov.deadlocks;

import java.util.Random;

public class DemonstrateDeadLock {

    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000000;

    public static void main(String[] args) {
        final Random rnd = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account();
        }

        class TransferThread extends Thread {
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int fromAcc = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcc = rnd.nextInt(NUM_ACCOUNTS);
                    DollarAmount amount = new DollarAmount(rnd.nextInt(1000));
                    try {
                        TransferMoney.transferMoney(accounts[fromAcc], accounts[toAcc], amount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }
    }
}
