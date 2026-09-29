package lab3.ui;

import lab3.exception.CourseCapacityExceededException;
import lab3.exception.EntityNotFoundException;
import lab3.model.Course;
import lab3.model.CourseFormat;
import lab3.model.Faculty;
import lab3.model.Student;
import lab3.service.UniversityService;

import java.io.Console;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
    private final UniversityService service;
    private final Scanner scanner;

    public ConsoleUI(UniversityService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        service.getLogger().log("--- ЗАПУСК СИСТЕМИ ---");

        while (running) {
            printMenu();
            String choice = scanner.nextLine();
            service.getLogger().log("Користувач обрав пункт меню: " + choice);

            try {
                switch (choice) {
                    case "1" -> displayCoursesPaginated();
                    case "2" -> addStudentUi();
                    case "3" -> addCourseUi();
                    case "4" -> enrollStudentUi();
                    case "5" -> gradeStudentUi();
                    case "6" -> showRecordBookUi();
                    case "7" -> generateReportUi();
                    case "8" -> demoWildcardUi();
                    case "9" -> importCsvUi();
                    case "10" -> exportReportUi();
                    case "11" -> runBenchmarkUi();
                    case "12" -> testTextAndBinaryIO();
                    case "13" -> createZipBackupUi();
                    case "14" -> restoreZipBackupUi();
                    case "15" -> cleanLogsUi();
                    // НОВІ ПУНКТИ ЛР 8
                    case "16" -> service.demoSorting();
                    case "17" -> service.demoTreeMap();
                    case "18" -> service.demoCustomIterator();
                    case "19" -> service.demoBinaryTree();
                    case "20" -> service.runCollectionsBenchmark();
                    case "0" -> {
                        service.saveAllData();
                        service.getLogger().log("--- СИСТЕМА ЗУПИНЕНА ---");
                        System.out.println("Дані збережено. Вихід...");
                        running = false;
                    }
                    default -> System.out.println("Невірний вибір.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Помилка: Введено текст замість числа!");
                service.getLogger().log("Помилка (невірний ввід числа).");
            } catch (EntityNotFoundException e) {
                System.out.printf("❌ Помилка бази: %s (Введений ID: %s)\n", e.getMessage(), e.getEntityId());
            } catch (CourseCapacityExceededException e) {
                System.out.printf("❌ Помилка лімітів: %s (Студент: %s)\n", e.getMessage(), e.getStudentId());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Помилка валідації: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Невідома системна помилка: " + e.getMessage());
            }

            if (running) waitForEnter();
        }
    }

    private void waitForEnter() {
        System.out.print("\n[Натисніть Enter, щоб повернутися до меню...]");
        scanner.nextLine();
    }

    private void printMenu() {
        System.out.println("\n\n===========================");
        System.out.println(" НАВЧАЛЬНА СИСТЕМА ");
        System.out.println("1. Список курсів");
        System.out.println("2. Додати студента");
        System.out.println("3. Додати курс");
        System.out.println("4. Записати студента на курс");
        System.out.println("5. Виставити оцінку");
        System.out.println("6. Залікова книжка студента");
        System.out.println("7. Звіт по персоналу");
        System.out.println("8. Демонстрація Wildcard (? extends Person)");
        System.out.println("9. Імпорт студента з CSV (Regex перевірка)");
        System.out.println("10. Експорт форматованого звіту у файл");
        System.out.println("11. Бенчмарк швидкості рядків (+ vs StringBuilder)");
        System.out.println("12. Експорт/Імпорт (Текст та Бінарний формат)");
        System.out.println("13. Створити резервну копію у ZIP");
        System.out.println("14. Відновити дані із ZIP (Потребує PIN)");
        System.out.println("15. Очистити старі логи (NIO.2 Files)");
        System.out.println("16. Демонстрація сортування (Comparable та Comparator)");
        System.out.println("17. Застосування TreeMap (групування за балом)");
        System.out.println("18. Демонстрація власного Ітератора (Iterable)");
        System.out.println("19. Демонстрація власного Бінарного дерева пошуку");
        System.out.println("20. Бенчмарк швидкості вставки (ArrayList vs LinkedList)");
        System.out.println("0. Вийти та зберегти");
        System.out.print("Оберіть дію: ");
    }

    private void displayCoursesPaginated() {
        List<Course> courses = service.getAllCourses();
        if (courses.isEmpty()) { System.out.println("Список курсів порожній."); return; }
        int pageSize = 2;
        int totalPages = (int) Math.ceil((double) courses.size() / pageSize);
        for (int page = 1; page <= totalPages; page++) {
            System.out.println("\n--- Курси: Сторінка " + page + " з " + totalPages + " ---");
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, courses.size());
            for (int i = start; i < end; i++) System.out.println(courses.get(i).toString());
            if (page < totalPages) {
                System.out.print("Натисніть Enter для наступної сторінки (або 'q'): ");
                if (scanner.nextLine().equalsIgnoreCase("q")) break;
            }
        }
    }

    private void addStudentUi() {
        System.out.print("ID студента: "); String id = scanner.nextLine();
        System.out.print("ПІБ: "); String name = scanner.nextLine();
        System.out.print("Факультет (1-ФІТІС, 2-ФЕУ, 3-ФГТ): ");
        String facChoice = scanner.nextLine();
        Faculty faculty = switch (facChoice) { case "2" -> Faculty.ECONOMICS; case "3" -> Faculty.HUMANITARIAN; default -> Faculty.IT; };
        service.addStudent(id, name, faculty);
        System.out.println("Студента успішно додано.");
    }

    private void addCourseUi() {
        System.out.print("ID курсу: "); String cId = scanner.nextLine();
        System.out.print("Назва курсу: "); String title = scanner.nextLine();
        System.out.print("ПІБ викладача: "); String tName = scanner.nextLine();
        System.out.print("Кафедра: "); String dep = scanner.nextLine();
        System.out.print("Формат (1-Онлайн, 2-Очний, 3-Змішаний): ");
        String formChoice = scanner.nextLine();
        CourseFormat format = switch (formChoice) { case "2" -> CourseFormat.OFFLINE; case "3" -> CourseFormat.HYBRID; default -> CourseFormat.ONLINE; };
        service.addCourse(cId, title, tName, dep, format);
        System.out.println("Курс успішно додано.");
    }

    private void enrollStudentUi() throws CourseCapacityExceededException {
        System.out.print("Введіть ID студента: "); String sId = scanner.nextLine();
        System.out.print("Введіть ID курсу: "); String cId = scanner.nextLine();
        service.enrollStudent(sId, cId);
        System.out.println("Записано.");
    }

    private void gradeStudentUi() {
        System.out.print("Введіть ID студента: "); String sId = scanner.nextLine();
        System.out.print("Введіть ID курсу: "); String cId = scanner.nextLine();
        System.out.print("Оцінка: "); int grade = Integer.parseInt(scanner.nextLine());
        service.gradeStudent(sId, cId, grade);
        System.out.println("Оцінку виставлено.");
    }

    private void showRecordBookUi() {
        System.out.print("Введіть ID студента: "); String sId = scanner.nextLine();
        Student student = service.findStudentById(sId);
        System.out.println("\n ЗАЛІКОВА КНИЖКА: " + student.getName());
        for (Map.Entry<String, Integer> entry : student.getGrades().entrySet()) {
            System.out.println("- " + service.findCourseById(entry.getKey()).getTitle() + ": " + entry.getValue());
        }
    }

    private void generateReportUi() { service.generateUniversityReport().forEach(r -> System.out.println(r.name() + " | " + r.role() + " | " + r.task())); }

    private void demoWildcardUi() {
        System.out.println("\n ДЕМОНСТРАЦІЯ WILDCARD ");
        List<Student> sampleStudents = List.of(new Student("T1", "Марія", Faculty.IT), new Student("T2", "Дмитро", Faculty.HUMANITARIAN));
        service.extractNames(sampleStudents).forEach(System.out::println);
    }

    private void importCsvUi() {
        System.out.println("Приклад: WEB-25; Омельян Діана Ігорівна; ФІТІС; omelandiana1@email.com; +380951234567");
        System.out.print("> "); String line = scanner.nextLine();
        service.importStudentFromCsvLine(line);
        System.out.println("Імпортовано!");
    }

    private void exportReportUi() { service.exportFormattedReport("report.txt"); }
    private void runBenchmarkUi() { service.runStringBenchmark(); }

    private void testTextAndBinaryIO() {
        service.exportDataToText("text_data.txt");
        service.exportDataToBinary("binary_data.dat");
        System.out.println("✅ Дані збережено.");
    }

    private void createZipBackupUi() {
        service.createZipBackup("binary_data.dat", "backup.zip");
        System.out.println("✅ Резервну копію створено.");
    }

    private void restoreZipBackupUi() {
        Console console = System.console();
        String pin = (console != null) ? new String(console.readPassword("🔒 PIN-код (1234): ")) : scanner.nextLine();
        if ("1234".equals(pin)) {
            service.restoreFromZipBackup("backup.zip", "binary_data.dat");
            System.out.println("✅ Дані відновлено!");
        } else { System.out.println("❌ Невірний PIN."); }
    }

    private void cleanLogsUi() { service.getLogger().cleanOldLogs(); }
}