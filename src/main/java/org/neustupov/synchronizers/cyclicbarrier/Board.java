package org.neustupov.synchronizers.cyclicbarrier;

public class Board {
    public void commitNewValues() {

    }

    public Board getSubBoard(int x, int y) {
        return new Board();
    }

    public boolean hasConverged() {
        return true;
    }

    public int getMaxX() {
        return 0;
    }

    public int getMaxY() {
        return 1;
    }

    public void setNewValue(int x, int y, int value) {
    }

    public void waitForConvergence() {
    }
}
