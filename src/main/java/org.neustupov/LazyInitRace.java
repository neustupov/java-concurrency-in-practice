package org.neustupov;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class LazyInitRace {

    private ExpensiveObject instance = null;
    public ExpensiveObject getInstance(){
        if(instance == null){
            instance = new ExpensiveObject();
        }
        return instance;
    }

    static class ExpensiveObject{}
}
