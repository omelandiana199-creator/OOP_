package lab3.pattern.observer;

import lab3.service.LoggerService;

public class FileLoggingObserver implements EventObserver {

    @Override
    public void onEvent(String eventType, String message) {
        String formattedMessage = String.format("[%s] %s", eventType, message);
        LoggerService.getInstance().info(formattedMessage);
    }
}