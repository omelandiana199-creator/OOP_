package lab3.pattern.observer;

@FunctionalInterface
public interface EventObserver {
    /**
     *
     * @param eventType тип події
     * @param message   детальний опис події
     */
    void onEvent(String eventType, String message);
}