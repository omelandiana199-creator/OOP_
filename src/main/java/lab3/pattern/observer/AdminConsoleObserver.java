package lab3.pattern.observer;

public class AdminConsoleObserver implements EventObserver {

    private static final String EVENT_URGENT = "URGENT";
    private static final String EVENT_ERROR = "ERROR";

    @Override
    public void onEvent(String eventType, String message) {
        if (EVENT_URGENT.equals(eventType) || EVENT_ERROR.equals(eventType)) {
            System.err.println("Спостерігач : Увага! " + message);
        }
    }
}