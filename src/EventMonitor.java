import java.util.Stack;
public class EventMonitor {

// Class for communicating between AI threads and the main thread
    private Stack<Event> stack;
    private Object lock;
    public EventMonitor() {
        stack = new Stack<>();
        lock = new Object();
    }

    public synchronized void push(Event event) {
        stack.push(event);
        notifyAll();
    }

    public synchronized Event pop() {
        while (stack.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        Event event = stack.pop();
        stack.clear();
        return event;
    }
}
