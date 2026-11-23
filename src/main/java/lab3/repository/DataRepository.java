package lab3.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class DataRepository {

    private final Path filePath;
    private final Gson gson;
    private UniversityData data;

    public DataRepository(String fileName) {
        Objects.requireNonNull(fileName, "Ім'я файлу не може бути null");

        this.filePath = Paths.get(fileName);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.data = loadData();
    }

    public UniversityData getData() {
        return data;
    }

    public void saveData() {
        try {
            String json = gson.toJson(data);
            Files.writeString(filePath, json, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Помилка збереження бази даних", e);
        }
    }

    private UniversityData loadData() {
        if (Files.exists(filePath)) {
            try {
                String json = Files.readString(filePath, StandardCharsets.UTF_8);
                return gson.fromJson(json, UniversityData.class);
            } catch (Exception e) {

                System.err.println("Помилка завантаження даних (" + e.getMessage() + "). Створюється нова база.");
            }
        }
        return new UniversityData();
    }
}