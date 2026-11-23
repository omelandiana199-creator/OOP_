package lab3.ui;

import lab3.exception.CourseCapacityExceededException;
import lab3.exception.EntityNotFoundException;
import lab3.model.Course;
import lab3.model.CourseFormat;
import lab3.model.Faculty;
import lab3.model.Student;
import lab3.pattern.facade.UniversityApiFacade;
import lab3.service.ConcurrencyDemoService;
import lab3.service.FileExportService;
import lab3.service.LoggerService;
import lab3.service.ReportService;
import lab3.service.UniversityService;
import lab3.functional.StudentFilter;

import java.io.Console;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final UniversityService universityService;
    private final FileExportService fileService;
    private final ReportService reportService;
    private final LoggerService logger;
    private final Scanner scanner;

    public ConsoleUI(UniversityService universityService,
                     FileExportService fileService,
                     ReportService reportService) {
        this.universityService = universityService;
        this.fileService = fileService;
        this.reportService = reportService;
        this.logger = LoggerService.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        logger.info("--- ЗАПУСК СИСТЕМИ ---");

        while (running) {
            printMenu();
            String choice = scanner.nextLine();
            logger.info("Користувач обрав пункт меню: " + choice);

            try {
                switch (choice) {
                    case "1" -> displayCoursesPaginated();
                    case "2" -> addStudentUi();
                    case "3" -> addCourseUi();
                    case "4" -> enrollStudentUi();
                    case "5" -> gradeStudentUi();
                    case "6" -> showRecordBookUi();
                    case "7" -> generateReportUi();
                    case "8" -> exportReportUi();
                    case "9" -> importCsvUi();
                    case "10" -> testTextAndBinaryIO();
                    case "11" -> createZipBackupUi();
                    case "12" -> restoreZipBackupUi();
                    case "13" -> cleanLogsUi();
                    case "14" -> deleteStudentUi();
                    case "15" -> demoCopyingUi();
                    case "16" -> findTopStudentUi();
                    case "17" -> printWildcardSummaryUi();
                    case "18" -> runBenchmarkUi();
                    case "19" -> demoLab8FeaturesUi();
                    case "20" -> runCollectionsBenchmarkUi();
                    case "21" -> demoStructuralPatternsUi();
                    case "22" -> demoLambdaAndStreamsUi();
                    case "23" -> demoConcurrencyUi();
                    case "0" -> {
                        universityService.saveAllData();
                        logger.info("--- СИСТЕМА ЗУПИНЕНА ---");
                        System.out.println("Дані збережено. Вихід...");
                        running = false;
                    }
                    default -> System.out.println("Невірний вибір.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Помилка: Введено текст замість числа!");
            } catch (EntityNotFoundException e) {
                System.out.printf("❌ Помилка бази: %s (Введений ID: %s)\n", e.getMessage(), e.getEntityId());
            } catch (CourseCapacityExceededException e) {
                System.out.printf("❌ Помилка лімітів: %s (Студент: %s)\n", e.getMessage(), e.getStudentId());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Помилка валідації: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Системна помилка: " + e.getMessage());
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
        System.out.println("1. Список курсів (Пагінація)");
        System.out.println("2. Додати студента");
        System.out.println("3. Додати курс");
        System.out.println("4. Записати студента на курс");
        System.out.println("5. Виставити оцінку");
        System.out.println("6. Залікова книжка студента");
        System.out.println("7. Звіт по персоналу");
        System.out.println("8. Експорт форматованого звіту у файл");
        System.out.println("9. Імпорт студента з CSV");
        System.out.println("10. Експорт (Текст та Бінарний формат)");
        System.out.println("11. Створити резервну копію у ZIP");
        System.out.println("12. Відновити дані із ZIP (Потребує PIN)");
        System.out.println("13. Очистити старі логи");
        System.out.println("14. Видалити студента");
        System.out.println("15. Демонстрація копіювання (ЛР4)");
        System.out.println("16. Знайти найкращого студента (Optional - ЛР5)");
        System.out.println("17. Загальний список (Wildcard - ЛР5)");
        System.out.println("18. Бенчмарк рядків (String vs StringBuilder - ЛР6)");
        System.out.println("19. Демонстрація колекцій, ітератора та дерева (ЛР8)");
        System.out.println("20. Бенчмарк колекцій (ArrayList vs LinkedList - ЛР8)");
        System.out.println("21. Структурні патерни (Facade + Decorator - GoF)");
        System.out.println("22. Демо Лямбда-виразів та Stream API (ЛР 12)");
        System.out.println("23. Демо Багатопотоковості та Синхронізації (ЛР 12)");
        System.out.println("0. Вийти та зберегти");
        System.out.print("Оберіть дію: ");
    }


    private void displayCoursesPaginated() {
        List<Course> courses = universityService.getDataRepository().getData().getCourses();
        if (courses.isEmpty()) { System.out.println("Список порожній."); return; }
        int pageSize = 2, totalPages = (int) Math.ceil((double) courses.size() / pageSize);
        for (int page = 1; page <= totalPages; page++) {
            System.out.println("\n--- Курси: Сторінка " + page + " з " + totalPages + " ---");
            for (int i = (page - 1) * pageSize; i < Math.min(page * pageSize, courses.size()); i++) {
                System.out.println(courses.get(i).toString());
            }
            if (page < totalPages) {
                System.out.print("Натисніть Enter для наступної сторінки (або 'q' для виходу): ");
                if (scanner.nextLine().equalsIgnoreCase("q")) break;
            }
        }
    }

    private void addStudentUi() {
        System.out.print("ID студента: "); String id = scanner.nextLine();
        System.out.print("ПІБ: "); String name = scanner.nextLine();
        System.out.print("Факультет (1-ФІТІС, 2-ФЕУ, 3-ФГТ): ");
        Faculty faculty = switch (scanner.nextLine()) { case "2" -> Faculty.ECONOMICS; case "3" -> Faculty.HUMANITARIAN; default -> Faculty.IT; };
        universityService.addStudent(id, name, faculty);
    }

    private void deleteStudentUi() {
        System.out.print("Введіть ID: "); universityService.removeStudent(scanner.nextLine());
    }

    private void addCourseUi() {
        System.out.print("ID курсу: "); String cId = scanner.nextLine();
        System.out.print("Назва: "); String title = scanner.nextLine();
        System.out.print("Викладач: "); String tName = scanner.nextLine();
        System.out.print("Кафедра: "); String dep = scanner.nextLine();
        universityService.addCourse(cId, title, tName, dep, CourseFormat.ONLINE);
    }

    private void enrollStudentUi() throws CourseCapacityExceededException {
        System.out.print("ID студента: "); String sId = scanner.nextLine();
        System.out.print("ID курсу: "); String cId = scanner.nextLine();
        universityService.enrollStudent(sId, cId);
    }

    private void gradeStudentUi() {
        System.out.print("ID студента: "); String sId = scanner.nextLine();
        System.out.print("ID курсу: "); String cId = scanner.nextLine();
        System.out.print("Оцінка: "); int grade = Integer.parseInt(scanner.nextLine());
        universityService.gradeStudent(sId, cId, grade);
    }

    private void showRecordBookUi() {
        System.out.print("ID студента: "); String sId = scanner.nextLine();
        Student student = universityService.findStudentById(sId);
        System.out.println("\n ЗАЛІКОВА КНИЖКА: " + student.getName());
        student.getGrades().forEach((k, v) -> System.out.println("- " + universityService.findCourseById(k).getTitle() + ": " + v));
    }

    private void generateReportUi() { reportService.generateUniversityReport().forEach(r -> System.out.println(r.name() + " | " + r.role())); }
    private void exportReportUi() { reportService.exportFormattedReport("report.txt"); }
    private void importCsvUi() { System.out.print("> CSV: "); fileService.importStudentFromCsvLine(scanner.nextLine()); }
    private void testTextAndBinaryIO() { fileService.exportDataToText("text_data.txt"); fileService.exportDataToBinary("binary_data.dat"); }
    private void createZipBackupUi() { fileService.createZipBackup("binary_data.dat", "backup.zip"); }

    private void restoreZipBackupUi() {
        Console console = System.console();
        String pin = (console != null) ? new String(console.readPassword("🔒 PIN (1234): ")) : scanner.nextLine();
        if ("1234".equals(pin)) { fileService.extractAndImportZipBackup("backup.zip", "restored_data.dat"); }
        else { System.out.println("❌ Невірний PIN."); }
    }

    private void cleanLogsUi() { logger.cleanOldLogs(); }

    private void demoCopyingUi() {
        Student original = new Student("S-999", "Іван", Faculty.IT);
        original.enrollCourse("JAVA"); original.setGrade("JAVA", 90);
        Student shallow = original.shallowCopy(); Student deep = original.deepCopy();
        shallow.setGrade("JAVA", 60); deep.setGrade("JAVA", 100);
        System.out.println("Оригінал після змін: " + original.getGrades());
        System.out.println("Поверхнева: " + shallow.getGrades());
        System.out.println("Глибока: " + deep.getGrades());
    }

    private void findTopStudentUi() {
        System.out.print("Факультет (1-ФІТІС, 2-ФЕУ, 3-ФГТ): ");
        Faculty faculty = switch (scanner.nextLine()) { case "2" -> Faculty.ECONOMICS; case "3" -> Faculty.HUMANITARIAN; default -> Faculty.IT; };
        universityService.findTopStudentByFaculty(faculty).ifPresentOrElse(
                student -> System.out.println("Найкращий: " + student.getName()), () -> System.out.println("Немає студентів.")
        );
    }

    private void printWildcardSummaryUi() { reportService.printPersonsSummary(universityService.getStudentRepo().findAll()); }

    private void runBenchmarkUi() {
        int iterations = 100000;
        long start1 = System.currentTimeMillis();
        String s = ""; for (int i = 0; i < iterations; i++) s += "A";
        long end1 = System.currentTimeMillis();
        long start2 = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder(); for (int i = 0; i < iterations; i++) sb.append("A");
        long end2 = System.currentTimeMillis();
        System.out.println("String (+): " + (end1 - start1) + " мс, StringBuilder: " + (end2 - start2) + " мс");
    }

    private void runCollectionsBenchmarkUi() {
        int count = 50000;
        List<Integer> al = new ArrayList<>(); List<Integer> ll = new LinkedList<>();
        for (int i = 0; i < count; i++) { al.add(i); ll.add(i); }
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < count/10; i++) al.add(count / 2, -1);
        long end1 = System.currentTimeMillis();
        long start2 = System.currentTimeMillis();
        java.util.ListIterator<Integer> it = ll.listIterator(count / 2);
        for (int i = 0; i < count/10; i++) it.add(-1);
        long end2 = System.currentTimeMillis();
        System.out.println("ArrayList: " + (end1 - start1) + " мс, LinkedList: " + (end2 - start2) + " мс");
    }

    private void demoLab8FeaturesUi() {
        universityService.sortStudentsDemo();
        universityService.showGradesTreeMapDemo();
    }

    private void demoStructuralPatternsUi() {
        UniversityApiFacade api = new UniversityApiFacade(universityService, fileService, reportService);
        api.registerNewStudent("DO-25", "Андрієнко Богдан", "IT");
        System.out.println(api.getFullyDecoratedReport());
    }

    // ЛР 12: Функціональні інтерфейси та Stream API
    private void demoLambdaAndStreamsUi() {
        System.out.println("\n--- ДЕМОНСТРАЦІЯ ЛЯМБДА-ВИРАЗІВ ТА STREAM API ---");

        // Використання власного @FunctionalInterface
        StudentFilter excellentStudents = student -> student.getAverageGrade() >= 90;
        long count = universityService.countStudentsByCustomFilter(excellentStudents);
        System.out.println("Кількість відмінників (через власний інтерфейс): " + count);

        // Вбудовані інтерфейси
        System.out.println("Студенти факультету ІТ (через вбудовані інтерфейси):");
        universityService.printStudentNamesWithBuiltInFunctions(s -> s.getFaculty() == Faculty.IT);
    }

    // Багатопотоковість
    private void demoConcurrencyUi() {
        ConcurrencyDemoService concurrencyService = new ConcurrencyDemoService();
        concurrencyService.demoRaceConditionAndSync();
        concurrencyService.demoProducerConsumer();
    }
}