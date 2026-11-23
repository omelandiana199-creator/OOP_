package lab3.service;

import lab3.model.Faculty;
import lab3.model.Student;
import lab3.model.Teacher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileExportService {

    private final UniversityService universityService;
    private final LoggerService logger = LoggerService.getInstance();

    // ЛР 6: регулярні вирази для валідації
    private static final String ID_PATTERN = "^S-\\d{3,4}$";
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";
    private static final String PHONE_PATTERN = "^\\+380\\d{9}$";

    public FileExportService(UniversityService universityService) {
        this.universityService = universityService;
    }

    public void exportDataToText(String filename) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename), StandardCharsets.UTF_8)) {
            writer.write("--- СТУДЕНТИ ---\n");
            for (Student s : universityService.getStudentRepo().findAll()) {
                writer.write(s.getId() + ";" + s.getName() + ";" + s.getFaculty().getDescription() + "\n");
            }
            writer.write("--- ВИКЛАДАЧІ ---\n");
            for (Teacher t : universityService.getDataRepository().getData().getTeachers()) {
                writer.write(t.getId() + ";" + t.getName() + ";" + t.getDepartment() + "\n");
            }
            universityService.getEventPublisher().notifyObservers("INFO", "Експорт в TXT: " + filename);
        } catch (IOException e) {
            logger.error("Помилка експорту в TXT", e);
        }
    }

    // ЛР 7: Завантаження даних у текстовому форматі через BufferedReader
    public void importDataFromText(String filename) {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            logger.info("Файл для імпорту не знайдено: " + filename);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("---") || line.isBlank()) continue;
                String[] parts = line.split(";");
                if (parts.length >= 3 && parts[0].startsWith("S-")) {
                    Faculty fac = parts[2].toLowerCase().contains("it") ? Faculty.IT : Faculty.HUMANITARIAN;
                    universityService.addStudent(parts[0], parts[1], fac);
                }
            }
            universityService.getEventPublisher().notifyObservers("INFO", "Дані імпортовано з TXT: " + filename);
        } catch (IOException e) {
            logger.error("Помилка читання TXT", e);
        }
    }

    public void exportDataToBinary(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(new ArrayList<>(universityService.getStudentRepo().findAll()));
            oos.writeObject(new ArrayList<>(universityService.getDataRepository().getData().getTeachers()));
            universityService.getEventPublisher().notifyObservers("INFO", "Експорт в BIN: " + filename);
        } catch (IOException e) {
            logger.error("Помилка бінарного експорту", e);
        }
    }

    public void importDataFromBinary(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Student> loadedStudents = (List<Student>) ois.readObject();
            List<Teacher> loadedTeachers = (List<Teacher>) ois.readObject();

            universityService.getStudentRepo().replaceAll(loadedStudents);

            List<Teacher> currentTeachers = universityService.getDataRepository().getData().getTeachers();
            currentTeachers.clear();
            currentTeachers.addAll(loadedTeachers);

            universityService.getEventPublisher().notifyObservers("INFO", "Дані відновлено з BIN.");
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Помилка бінарного імпорту", e);
        }
    }

    public void createZipBackup(String sourceFile, String zipFile) {
        exportDataToBinary(sourceFile);
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(sourceFile)) {

            zos.putNextEntry(new ZipEntry(sourceFile));
            byte[] buffer = new byte[4096];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            universityService.getEventPublisher().notifyObservers("INFO", "ZIP-архів створено.");
        } catch (IOException e) {
            logger.error("Помилка створення ZIP", e);
        }
    }

    // ЛР 7: Відновлення даних з ZIP-архіву
    public void extractAndImportZipBackup(String zipFile, String destFile) {
        Path zipPath = Paths.get(zipFile);
        if (!Files.exists(zipPath)) {
            System.out.println("❌ Архів не знайдено!");
            return;
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
             FileOutputStream fos = new FileOutputStream(destFile)) {

            ZipEntry entry = zis.getNextEntry();
            if (entry != null) {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = zis.read(buffer)) >= 0) {
                    fos.write(buffer, 0, length);
                }
                zis.closeEntry();
            }
            importDataFromBinary(destFile);
        } catch (IOException e) {
            logger.error("Помилка розпакування ZIP", e);
        }
    }

    public void importStudentFromCsvLine(String line) {
        if (line == null || line.isBlank()) return;

        String cleanLine = line.trim();
        cleanLine = cleanLine.replace("\"", "");

        if (!cleanLine.contains(";")) {
            throw new IllegalArgumentException("Невірний роздільник CSV!");
        }

        String[] parts = cleanLine.split(";");
        if (parts.length < 5) throw new IllegalArgumentException("Недостатньо даних у рядку CSV");

        String id = parts[0].trim().toUpperCase();
        String name = parts[1].trim();
        String facChoice = parts[2].trim();
        String email = parts[3].trim().toLowerCase();
        String phone = parts[4].trim();

        if (!id.matches(ID_PATTERN)) throw new IllegalArgumentException("Невірний формат ID: " + id);
        if (!email.matches(EMAIL_PATTERN)) throw new IllegalArgumentException("Невірний формат Email: " + email);
        if (!phone.matches(PHONE_PATTERN)) throw new IllegalArgumentException("Невірний формат телефону: " + phone);

        Faculty faculty = switch (facChoice) {
            case "ФЕУ" -> Faculty.ECONOMICS;
            case "ФГТ" -> Faculty.HUMANITARIAN;
            default -> Faculty.IT;
        };

        universityService.addStudent(id, name, faculty);
    }
}