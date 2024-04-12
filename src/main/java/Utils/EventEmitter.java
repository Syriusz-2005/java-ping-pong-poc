package Utils;

import java.util.ArrayList;

public class EventEmitter<T> {
    private final ArrayList<Listener<T>> listeners = new ArrayList<>();
    private final ArrayList<Listener<T>> onceListeners = new ArrayList<>();


    protected void emit(T event) {
        for (var listener : listeners) {
            listener.handle(event);
        }
        for (var listener : onceListeners) {
            listener.handle(event);
            onceListeners.remove(listener);
        }
    }

    public void on(Listener<T> listener) {
        listeners.add(listener);
    }

    public void once(Listener<T> listener) {
        onceListeners.add(listener);
    }
}
