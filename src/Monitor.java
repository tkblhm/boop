// Class for communicating between AI threads and the main thread
public class Monitor<T> {
    private T[] slots;
    private boolean end;
    private Object lock;
    public Monitor() {
        slots = (T[]) new Object[2];
        end = false;
        lock = new Object();
    }

    public synchronized void setSlot(T move, int slot) {
        while (slots[slot] != null) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        slots[slot] = move;
        notifyAll();
    }

    public synchronized T getSlot(int slot) {
        while (slots[slot] == null) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        T move = slots[slot];
        slots[slot] = null;
        notifyAll();
        return move;
    }

    public void setEnd() {
        synchronized (lock) {
            end = true;
        }
    }

    public boolean getEnd() {
        synchronized (lock) {
            return end;
        }
    }
}
