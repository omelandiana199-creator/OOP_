package lab3.pattern.observer;

import java.util.ArrayList;
import java.util.List;

public class UniversityEventPublisher {
    private final List<EventObserver> observers = new ArrayList<>();

    public void subscribe(EventObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(EventObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String eventType, String message) {
        for (EventObserver observer : observers) {
            observer.onEvent(eventType, message);
        }
    }
}