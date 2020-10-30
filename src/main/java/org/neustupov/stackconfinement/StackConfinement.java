package org.neustupov.stackconfinement;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class StackConfinement {

    Ark ark = new Ark();

    public int loadTheArc(Collection<Animal> candidates) {
        SortedSet<Animal> animals;
        int numPairs = 0;
        Animal candidate = null;

        animals = new TreeSet<Animal>();
        animals.addAll(candidates);

        for (Animal a : animals) {
            if (candidate == null || !candidate.isPotentialMate(a)) {
                candidate = a;
            } else {
                ark.load(new AnimalPair(candidate, a));
                ++numPairs;
                candidate = null;
            }
        }

        return numPairs;
    }
}
