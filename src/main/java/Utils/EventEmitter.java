package Utils;

import java.util.ArrayList;

public class EventEmitter<T> {
    private transient final ArrayList<Listener<T>> listeners = new ArrayList<>();
    private transient final ArrayList<Listener<T>> onceListeners = new ArrayList<>();


    protected void emit(T event) {
        if (listeners != null) {
            for (var listener : listeners) {
                listener.handle(event);
            }
        }

        if (onceListeners != null) {
            for (var listener : onceListeners) {
                listener.handle(event);
                onceListeners.remove(listener);
            }
        }
    }

    public void on(Listener<T> listener) {
        listeners.add(listener);
    }

    public void once(Listener<T> listener) {
        onceListeners.add(listener);
    }
}
