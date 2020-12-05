package org.neustupov.deadlocks;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.TimeUnit;

public class TransferMoney {

    private static final Object tieLock = new Object();

    public static void transferMoney(final Account fromAcc, final Account toAcc, final DollarAmount amount) throws Exception {
        class Helper {
            public void transfer() throws Exception {
                if (fromAcc.getBalance().compareTo(amount) < 0) {
                    throw new Exception("Bad balance");
                } else {
                    fromAcc.debit(amount);
                    toAcc.credit(amount);
                }
            }
        }
        int fromHash = System.identityHashCode(fromAcc);
        int toHash = System.identityHashCode(toAcc);

        if (fromHash < toHash) {
            synchronized (fromAcc) {
                synchronized (toAcc) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAcc) {
                synchronized (fromAcc) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAcc) {
                    synchronized (toAcc) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }

    public static boolean transferMoneyWithLocks(Account fromAcc, Account toAcc, DollarAmount amount,
                                                 long timeuot, TimeUnit unit) {
        while (true) {
            if (fromAcc.lock.tryLock()) {
                try {
                    if (toAcc.lock.tryLock()) {
                        try {
                            if (fromAcc.getBalance().compareTo(amount) < 0) {
                                throw new RuntimeException();
                            } else {
                                fromAcc.debit(amount);
                                toAcc.credit(amount);
                                return true;
                            }
                        } finally {
                            toAcc.lock.unlock();
                        }
                    }
                } finally {
                    fromAcc.lock.unlock();
                }
            }
        }
    }
}
