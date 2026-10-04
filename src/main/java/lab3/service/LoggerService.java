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
    private static final int MAX_LINES = 10;
    private static final String LOG_DIR = "logs";
    private static final String LOG_PREFIX = "app_log_";
    private static final String LOG_EXTENSION = ".txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int currentLines = 0;
    private int fileIndex = 1;

    public LoggerService() {
        try {
            Files.createDirectories(Paths.get(LOG_DIR));
        } catch (IOException e) {
            System.err.println("Помилка створення папки логів: " + e.getMessage());
        }
    }

    public synchronized void log(String message) {
        Path logFile = Paths.get(LOG_DIR, LOG_PREFIX + fileIndex + LOG_EXTENSION);

        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(LocalDateTime.now().format(FORMATTER) + " [INFO] : " + message);
            writer.newLine();
            currentLines++;

            if (currentLines >= MAX_LINES) {
                currentLines = 0;
                fileIndex++; // Ротація логів
            }
        } catch (IOException e) {
            System.err.println("Помилка запису лога: " + e.getMessage());
        }
    }

    public synchronized void cleanOldLogs() {
        String globPattern = LOG_PREFIX + "*" + LOG_EXTENSION;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LOG_DIR), globPattern)) {
            for (Path path : stream) {
                long size = Files.size(path);
                Files.delete(path);
                System.out.println("Видалено старий лог: " + path.getFileName() + " (" + size + " байт)");
            }
            // Скидання лічильників після очищення
            fileIndex = 1;
            currentLines = 0;
            System.out.println("✅ Папку логів успішно очищено.");
        } catch (IOException e) {
            System.err.println("❌ Помилка очищення логів: " + e.getMessage());
        }
    }
}