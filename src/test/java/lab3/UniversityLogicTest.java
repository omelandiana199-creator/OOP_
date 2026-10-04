package lab3;

import lab3.exception.CourseCapacityExceededException;
import lab3.exception.EntityNotFoundException;
import lab3.model.Course;
import lab3.model.CourseFormat;
import lab3.model.Faculty;
import lab3.model.Student;
import lab3.model.Teacher;
import lab3.repository.DataRepository;
import lab3.service.UniversityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UniversityLogicTest {

    private Student student;
    private Teacher teacher;
    private Course course;
    private UniversityService service;

    @BeforeEach
    void setUp() {
        // Ініціалізація базових об'єктів для тестів
        student = new Student("S-01", "Олександр Сергійович", Faculty.IT);
        teacher = new Teacher("T-01", "Василь Іванович", "Кафедра інженерії програмного забезпечення");
        course = new Course("C-01", "Об'єктно-орієнтоване програмування", teacher, CourseFormat.ONLINE);

        DataRepository repository = new DataRepository("test_db.json");
        service = new UniversityService(repository);
    }

    @Test
    void testPersonRolePolymorphism() {
        assertEquals("Студент", student.getRole(),
                "Роль студента має повертати 'Студент'");
        assertEquals("Викладач (Кафедра інженерії програмного забезпечення)", teacher.getRole(),
                "Роль викладача має коректно формуватися з назвою кафедри");
    }

    @Test
    void testCourseCreation() {
        assertEquals("C-01", course.getId());
        assertEquals("Об'єктно-орієнтоване програмування", course.getTitle());
    }

    @Test
    void testStudentGradesLogic() {
        // Перевірка запису на курс
        student.enrollCourse("C-01");
        assertTrue(student.getGrades().containsKey("C-01"), "Після запису курс має з'явитися у мапі студента");
        assertEquals(0, student.getGrades().get("C-01"), "Початкова оцінка має дорівнювати 0");

        // Перевірка виставлення оцінки
        student.setGrade("C-01", 95);
        assertEquals(95, student.getGrades().get("C-01"), "Оцінка має успішно оновитися");

        // Перевірка захисту від виставлення оцінки без запису на курс
        student.setGrade("C-99", 100);
        assertFalse(student.getGrades().containsKey("C-99"), "Оцінка не повинна додаватися для незаписаного курсу");
    }


    @Test
    void testEntityNotFoundException() {
        // Перевіряємо, чи викидається правильний виняток при пошуку неіснуючого об'єкта
        assertThrows(EntityNotFoundException.class, () -> service.findStudentById("UNKNOWN_ID"),
                "Має викидатися EntityNotFoundException для неіснуючого студента");
    }

    @Test
    void testCourseCapacityExceededException() throws CourseCapacityExceededException {
        // Створюємо студента та 6 курсів
        service.addStudent("S-99", "Ліміт Тест", Faculty.IT);
        for (int i = 1; i <= 6; i++) {
            service.addCourse("COURSE-" + i, "Тестовий курс " + i, "Викладач", "Кафедра", CourseFormat.ONLINE);
        }

        // Записуємо на 5 курсів (має пройти успішно)
        for (int i = 1; i <= 5; i++) {
            service.enrollStudent("S-99", "COURSE-" + i);
        }

        // Спроба записатися на 6-й курс має викликати наш власний виняток
        assertThrows(CourseCapacityExceededException.class, () -> service.enrollStudent("S-99", "COURSE-6"),
                "Має спрацювати ліміт на 5 курсів");
    }

    @Test
    void testValidCsvImport() {
        String validCsv = "S-123; Іванов Іван; ФІТІС; test.student@email.com; +380991234567";
        // Метод не повинен викидати жодних помилок
        assertDoesNotThrow(() -> service.importStudentFromCsvLine(validCsv));

        // Перевіряємо, чи студент дійсно з'явився в базі
        Student imported = service.findStudentById("S-123");
        assertNotNull(imported);
        assertEquals("Іванов Іван", imported.getName());
    }

    @Test
    void testInvalidCsvImportThrowsException() {
        String invalidEmailCsv = "S-124; Петро; ФІТІС; bad-email; +380991234567";
        String invalidPhoneCsv = "S-125; Ганна; ФІТІС; hanna@email.com; 0991234567"; // Немає +380

        assertThrows(IllegalArgumentException.class, () -> service.importStudentFromCsvLine(invalidEmailCsv),
                "Має викидатися помилка через невірний формат Email");
        assertThrows(IllegalArgumentException.class, () -> service.importStudentFromCsvLine(invalidPhoneCsv),
                "Має викидатися помилка через невірний формат телефону");
    }

    @Test
    void testStudentComparableNaturalOrdering() {
        Student s1 = new Student("S-20", "Яна", Faculty.IT);
        Student s2 = new Student("S-21", "Анна", Faculty.ECONOMICS);
        Student s3 = new Student("S-22", "Богдан", Faculty.HUMANITARIAN);

        List<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);

        Collections.sort(students);

        assertEquals("Анна", students.get(0).getName(), "Анна має бути першою за алфавітом");
        assertEquals("Богдан", students.get(1).getName());
        assertEquals("Яна", students.get(2).getName(), "Яна має бути останньою");
    }

    @Test
    void testAverageGradeCalculation() {
        student.enrollCourse("C-1");
        student.enrollCourse("C-2");
        student.setGrade("C-1", 100);
        student.setGrade("C-2", 90);

        assertEquals(95.0, student.getAverageGrade(), 0.01,
                "Середній бал має вираховуватися коректно (190 / 2 = 95)");
    }
}