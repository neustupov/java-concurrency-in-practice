package org.neustupov.stackconfinement;

public class Animal implements Comparable{

    public boolean isPotentialMate(Animal animal){
        return animal.equals(this);
    }

    public int compareTo(Object o) {
        return 0;
    }
}
