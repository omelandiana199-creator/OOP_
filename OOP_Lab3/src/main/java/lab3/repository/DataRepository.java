package lab3.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataRepository {
    private final Path filePath;
    private UniversityData data;
    private final Gson gson;

    public DataRepository(String fileName) {
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
            Files.writeString(filePath, json);
        } catch (Exception e) {
            throw new RuntimeException("Помилка збереження бази даних: " + e.getMessage());
        }
    }

    private UniversityData loadData() {
        if (Files.exists(filePath)) {
            try {
                String json = Files.readString(filePath);
                return gson.fromJson(json, UniversityData.class);
            } catch (Exception e) {
                System.err.println("Помилка завантаження даних, створюється нова база.");
            }
        }
        return new UniversityData();
    }
}