package org.neustupov.deadlocks;

public class DollarAmount implements Comparable {
    private int amount;

    public DollarAmount(int amount) {
        this.amount = amount;
    }

    public DollarAmount() {
    }

    @Override
    public int compareTo(Object o) {
        int thisAmount = getAmount();
        int otherAmount = ((DollarAmount) o).getAmount();
        if (thisAmount > otherAmount) {
            return 1;
        } else if (thisAmount < otherAmount) {
            return -1;
        }
        return 0;
    }

    public int getAmount() {
        return amount;
    }
}
