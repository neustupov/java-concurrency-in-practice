package org.neustupov.immutable;

import net.jcip.annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

@Immutable
public class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>();

    public ThreeStooges() {
        stooges.add("A");
        stooges.add("B");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }
}
