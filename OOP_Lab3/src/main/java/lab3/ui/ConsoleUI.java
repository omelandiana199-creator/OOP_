package lab3.ui;

import lab3.exception.CourseCapacityExceededException;
import lab3.exception.EntityNotFoundException;
import lab3.model.*;
import lab3.service.UniversityService;

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
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            // ГЛОБАЛЬНА ОБРОБКА ВИНЯТКІВ
            try {
                switch (choice) {
                    case "1" -> displayCoursesPaginated();
                    case "2" -> addStudentUi();
                    case "3" -> addCourseUi();
                    case "4" -> enrollStudentUi();
                    case "5" -> gradeStudentUi();
                    case "6" -> showRecordBookUi();
                    case "7" -> generateReportUi();
                    case "8" -> demoWildcardUi(); // Демонстрація Wildcard (? extends Person)
                    case "0" -> {
                        service.saveAllData();
                        System.out.println("Дані збережено. Вихід...");
                        running = false;
                    }
                    default -> System.out.println("Невірний вибір.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Помилка: Введено текст замість числа! Деталі: " + e.getMessage());
            } catch (EntityNotFoundException e) {
                System.out.printf("❌ Помилка бази: %s (Введений ID: %s)\n", e.getMessage(), e.getEntityId());
            } catch (CourseCapacityExceededException e) {
                System.out.printf("❌ Помилка лімітів: %s (Студент: %s)\n", e.getMessage(), e.getStudentId());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Помилка логіки: " + e.getMessage());
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
        System.out.println("4. Записати студента на курс ");
        System.out.println("5. Виставити оцінку ");
        System.out.println("6. Залікова книжка студента");
        System.out.println("7. Звіт по персоналу");
        System.out.println("8. Демонстрація Wildcard (? extends Person) ");
        System.out.println("0. Вийти та зберегти");
        System.out.print("Оберіть дію: ");
    }

    private void displayCoursesPaginated() {
        List<Course> courses = service.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("Список курсів порожній.");
            return;
        }
        int pageSize = 2;
        int totalPages = (int) Math.ceil((double) courses.size() / pageSize);
        for (int page = 1; page <= totalPages; page++) {
            System.out.println("\n--- Курси: Сторінка " + page + " з " + totalPages + " ---");
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, courses.size());
            for (int i = start; i < end; i++) {
                System.out.println(courses.get(i).toString());
            }
            if (page < totalPages) {
                System.out.print("Натисніть Enter для наступної сторінки (або 'q' для виходу: ");
                if (scanner.nextLine().equalsIgnoreCase("q")) break;
            }
        }
    }

    private void addStudentUi() {
        System.out.print("ID студента: "); String id = scanner.nextLine();
        System.out.print("ПІБ: "); String name = scanner.nextLine();
        System.out.println("Оберіть факультет (1-ФІТІС, 2-ФЕУ, 3-ФГТ): ");
        String facChoice = scanner.nextLine();
        Faculty faculty = switch (facChoice) {
            case "2" -> Faculty.ECONOMICS;
            case "3" -> Faculty.HUMANITARIAN;
            default -> Faculty.IT;
        };
        service.addStudent(id, name, faculty);
        System.out.println("Студента успішно додано.");
    }

    private void addCourseUi() {
        System.out.print("ID курсу: "); String cId = scanner.nextLine();
        System.out.print("Назва курсу: "); String title = scanner.nextLine();
        System.out.print("ПІБ викладача: "); String tName = scanner.nextLine();
        System.out.print("Кафедра викладача: "); String dep = scanner.nextLine();
        System.out.println("Формат (1-Онлайн, 2-Очний, 3-Змішаний): ");
        String formChoice = scanner.nextLine();
        CourseFormat format = switch (formChoice) {
            case "2" -> CourseFormat.OFFLINE;
            case "3" -> CourseFormat.HYBRID;
            default -> CourseFormat.ONLINE;
        };
        service.addCourse(cId, title, tName, dep, format);
        System.out.println("Курс успішно додано.");
    }

    private void enrollStudentUi() throws CourseCapacityExceededException {
        System.out.print("Введіть ID студента: "); String sId = scanner.nextLine();
        System.out.print("Введіть ID курсу: "); String cId = scanner.nextLine();
        service.enrollStudent(sId, cId);
        System.out.println("Студента успішно записано на курс.");
    }

    private void gradeStudentUi() {
        System.out.print("Введіть ID студента: "); String sId = scanner.nextLine();
        System.out.print("Введіть ID курсу: "); String cId = scanner.nextLine();
        System.out.print("Введіть оцінку: ");
        int grade = Integer.parseInt(scanner.nextLine());
        service.gradeStudent(sId, cId, grade);
        System.out.println("Оцінку успішно виставлено.");
    }

    private void showRecordBookUi() {
        System.out.print("Введіть ID студента: "); String sId = scanner.nextLine();
        Student student = service.findStudentById(sId);
        System.out.println("\n ЗАЛІКОВА КНИЖКА ");
        System.out.println("Студент: " + student.getName() + " | Факультет: " + student.getFaculty().getName());
        for (Map.Entry<String, Integer> entry : student.getGrades().entrySet()) {
            Course course = service.findCourseById(entry.getKey());
            String gradeStr = (entry.getValue() > 0) ? String.valueOf(entry.getValue()) : "Немає оцінки";
            System.out.println("- " + course.getTitle() + ": " + gradeStr);
        }
    }

    private void generateReportUi() {
        service.generateUniversityReport().forEach(r -> {
            System.out.println(r.name() + " | " + r.role() + " | " + r.task());
        });
    }

    private void demoWildcardUi() {
        System.out.println("\n ДЕМОНСТРАЦІЯ WILDCARD (? extends Person) ");
        // Створюємо окремий список винятково зі студентів
        List<Student> sampleStudents = List.of(
                new Student("TEMP1", "Марія", Faculty.IT),
                new Student("TEMP2", "Дмитро", Faculty.HUMANITARIAN)
        );
        // Метод успішно приймає List<Student>, хоча очікує List<? extends Person>
        List<String> names = service.extractNames(sampleStudents);
        names.forEach(System.out::println);
    }
}