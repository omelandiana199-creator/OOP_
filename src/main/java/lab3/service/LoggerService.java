package lab3.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerService {
    // Потокостійкий Singleton
    private static volatile LoggerService instance;
    private final Path logDir;
    private Path currentLogFile;
    private int lineCount = 0;
    private static final int MAX_LINES = 50;

    private LoggerService() {
        logDir = Paths.get("logs");
        try {
            // NIO.2: Створення директорії, якщо її немає
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            createNewLogFile();
        } catch (IOException e) {
            System.err.println("Помилка ініціалізації логера: " + e.getMessage());
        }
    }

    public static LoggerService getInstance() {
        if (instance == null) {
            synchronized (LoggerService.class) {
                if (instance == null) {
                    instance = new LoggerService();
                }
            }
        }
        return instance;
    }

    private void createNewLogFile() throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        currentLogFile = logDir.resolve("app_log_" + timestamp + ".txt");
        Files.createFile(currentLogFile);
        lineCount = 0;
    }

    private void writeLog(String level, String message) {
        if (currentLogFile == null) return;

        String logEntry = String.format("[%s] %s: %s\n", LocalDateTime.now(), level, message);

        // Буферизований потік запису
        try (BufferedWriter writer = Files.newBufferedWriter(currentLogFile,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(logEntry);
            lineCount++;

            // Ротація логів
            if (lineCount >= MAX_LINES) {
                createNewLogFile();
            }
        } catch (IOException e) {
            System.err.println("Помилка запису логу: " + e.getMessage());
        }
    }

    public void info(String message) { writeLog("INFO", message); }
    public void error(String message, Exception e) { writeLog("ERROR", message + " | " + e.getMessage()); }

    // NIO.2: Перевірка розміру та видалення застарілих файлів
    public void cleanOldLogs() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logDir, "app_log_*.txt")) {
            for (Path entry : stream) {
                if (!entry.equals(currentLogFile)) {
                    long size = Files.size(entry);
                    Files.deleteIfExists(entry);
                    System.out.println("Видалено старий лог: " + entry.getFileName() + " (Розмір: " + size + " байт)");
                }
            }
            System.out.println("Очищення логів завершено.");
        } catch (IOException e) {
            error("Помилка очищення логів", e);
        }
    }
}