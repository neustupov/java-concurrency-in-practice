package org.neustupov.boundedbuffer;

public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer {

    public GrumpyBoundedBuffer(int size){
        super(size);
    }

    public synchronized void put(V v) throws BufferFullException {
        if(isFull()){
            throw new BufferFullException();
        }
        doPut(v);
    }

    public synchronized V take() throws BufferEmptyException {
        if(isEmpty()){
            throw new BufferEmptyException();
        }
        return (V) doTake();
    }
}
