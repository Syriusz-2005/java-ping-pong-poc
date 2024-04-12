package Utils;

@FunctionalInterface
public interface Listener<T> {
    void handle(T event);
}
