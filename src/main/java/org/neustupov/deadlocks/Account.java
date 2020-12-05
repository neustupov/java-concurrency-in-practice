package org.neustupov.deadlocks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    public Lock lock = new ReentrantLock();

    public DollarAmount getBalance(){
        return new DollarAmount();
    }

    public void debit(DollarAmount amount){

    }

    public void credit(DollarAmount amount){

    }
}
