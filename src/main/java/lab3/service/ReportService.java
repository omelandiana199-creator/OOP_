package lab3.service;

import lab3.dto.PersonInfo;
import lab3.model.Person;
import lab3.model.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    private final UniversityService universityService;
    private final LoggerService logger = LoggerService.getInstance();

    public ReportService(UniversityService universityService) {
        this.universityService = universityService;
    }

    public List<PersonInfo> generateUniversityReport() {
        List<Person> allPeople = new ArrayList<>();
        allPeople.addAll(universityService.getDataRepository().getData().getTeachers());
        allPeople.addAll(universityService.getStudentRepo().findAll());

        List<PersonInfo> report = new ArrayList<>();
        for (Person p : allPeople) {
            String roleDetails = p instanceof Student s ? "Вчиться: " + s.getFaculty().getDescription() : "Викладає";
            report.add(new PersonInfo(p.getName(), roleDetails, p.getDailyTask()));
        }
        return report;
    }

    public void exportFormattedReport(String filename) {
        StringBuilder sb = new StringBuilder();

        // ЛР 6: Операції append
        sb.append("***************************************************************\n")
                .append("*").append("                              ЗВІТ").append("                       *\n")
                .append("*").append("                      УНІВЕРСИТЕТУ").append("                         *\n")
                .append("***************************************************************\n")
                .append("\n")
                .append("---------------------------------------------------------------\n")
                .append("|")
                // ЛР 6: Використання String.format для вирівнювання
                .append(String.format(" %-10.10s ", "ID"))
                .append("|")
                .append(String.format(" %-25.25s ", "ПІП"))
                .append("|")
                .append(String.format(" %-18.18s ", "Роль"))
                .append("|\n")
                .append("---------------------------------------------------------------\n");

        for (Student s : universityService.getStudentRepo().findAll()) {
            sb.append("|")
                    .append(String.format(" %-10.10s ", s.getId()))
                    .append("|")
                    .append(String.format(" %-25.25s ", s.getName()))
                    .append("|")
                    .append(String.format(" %-18.18s ", "Студент"))
                    .append("|\n");
        }
        sb.append("===============================================================\n");

        // ЛР 6: Використання PrintWriter для виведення у файл
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.print(sb.toString()); // Запис у файл
            System.out.println(sb.toString()); // Вивід форматованого звіту в консоль
            System.out.println("✅ Звіт успішно згенеровано та збережено у " + filename);
        } catch (IOException e) {
            logger.error("Помилка запису звіту", e);
        }
    }

    // ЛР 5: Узагальнений метод з використанням wildcard (? extends)
    public void printPersonsSummary(List<? extends Person> people) {
        System.out.println("\n--- ЗАГАЛЬНИЙ СПИСОК (Wildcard Обробка) ---");
        for (Person p : people) {
            // Поліморфний виклик методів для будь-якого нащадка Person (Student чи Teacher)
            System.out.printf("ID: %s | Роль: %s | ПІБ: %s\n", p.getId(), p.getRole(), p.getName());
        }
    }
}